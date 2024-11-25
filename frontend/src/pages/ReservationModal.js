import React, { useState, useEffect } from "react";
import axios from "axios";
import DatePicker from "react-datepicker";
import { ko } from "date-fns/locale";
import "react-datepicker/dist/react-datepicker.css";
import "./ReservationModal.css";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

// CustomAlert 컴포넌트
const CustomAlert = ({ message, type }) => {
  return (
    <div className="alert-overlay">
      <div className={`alert-container alert-${type}`}>
        <span className="alert-icon">
          {type === "success" ? "✓" : type === "warning" ? "!" : "✕"}
        </span>
        <span className="alert-message">{message}</span>
      </div>
    </div>
  );
};

// CustomConfirm 컴포넌트
const CustomConfirm = ({ message, onConfirm, onCancel }) => {
  return (
    <div className="confirm-overlay">
      <div className="confirm-container">
        <h3 className="confirm-title">예약 확인</h3>
        <p className="confirm-message">{message}</p>
        <div className="confirm-buttons">
          <button className="confirm-button confirm-cancel" onClick={onCancel}>
            취소
          </button>
          <button className="confirm-button confirm-ok" onClick={onConfirm}>
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

const ReservationModal = ({ isOpen, onClose, selectedHospital }) => {
  const navigate = useNavigate();
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState(null);
  const [reservationTimes, setReservationTimes] = useState([]);
  const [providerId, setProviderId] = useState({ id: "" });
  const [alert, setAlert] = useState({ show: false, message: "", type: "" });
  const [showConfirm, setShowConfirm] = useState(false);
  const token = localStorage.getItem("token");
  let redisKey = "";

  useEffect(() => {
    if (isOpen) {
      setProviderId({ id: jwtDecode(token).userId });
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const showAlert = (message, type) => {
    setAlert({ show: true, message, type });
    setTimeout(() => {
      setAlert({ show: false, message: "", type: "" });
    }, 2000);
  };

  const convertDate = (date) => {
    const offsetDate = new Date(
      date.getTime() - date.getTimezoneOffset() * 60000
    );
    const formattedDate = offsetDate.toISOString().split("T")[0];
    return formattedDate;
  };

  const availableTimes = [
    "09:00",
    "09:30",
    "10:00",
    "10:30",
    "11:00",
    "11:30",
    "13:00",
    "13:30",
    "14:00",
    "14:30",
    "15:00",
    "15:30",
    "16:00",
    "16:30",
    "17:00",
    "17:30",
  ];

  const isTimeDisabled = (time) => {
    if (!selectedDate) return true;

    const today = new Date();
    const selectedDay = new Date(selectedDate);

    if (
      selectedDay.getDate() === today.getDate() &&
      selectedDay.getMonth() === today.getMonth() &&
      selectedDay.getFullYear() === today.getFullYear()
    ) {
      const [hours, minutes] = time.split(":").map(Number);
      const currentHours = today.getHours();
      const currentMinutes = today.getMinutes();

      return (
        currentHours > hours ||
        (currentHours === hours && currentMinutes > minutes)
      );
    }

    return false;
  };

  const getReservation = async (formattedDate) => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreservation?reservationAt=${formattedDate}&hospId=${selectedHospital.hospId}`
      );
      if (response.data.success) {
        const newReservationTime = response.data.data.map(
          (item) => item.reservationTime
        );
        setReservationTimes(newReservationTime);
      }
    } catch (error) {
      showAlert("예약 정보를 불러오는데 실패했습니다.", "error");
    }
  };

  const handleReservation = () => {
    if (!selectedDate || !selectedTime) {
      showAlert("예약 날짜와 시간을 선택해주세요.", "warning");
      return;
    }
    setShowConfirm(true);
  };

  const handleConfirm = () => {
    setShowConfirm(false);
    createReservation();
  };

  const createReservation = () => {
    const selctConvertDate = convertDate(selectedDate);
    const resercationData = {
      hospId: selectedHospital.hospId,
      reservationAt: selctConvertDate,
      reservationTime: selectedTime,
    };

    axios
      .post(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreservation`,
        resercationData
      )
      .then((response) => {
        if (!response.data.data.isvalid) {
          redisKey = response.data.data.redisKey;
          requestPay(redisKey);
        } else {
          showAlert("결제에 실패했습니다. 다시 시도해주세요.", "error");
        }
      })
      .catch((error) => {
        showAlert("진행 중인 결제가 있습니다. 다시 시도해주세요.", "error");
      });
  };

  const requestPay = (redisKey) => {
    if (!window.IMP) {
      showAlert("결제 모듈을 불러오는데 실패했습니다.", "error");
      return;
    }

    const { IMP } = window;
    IMP.init("imp44914623");
    IMP.request_pay(
      {
        pg: "html5_inicis",
        pay_method: "card",
        name: selectedHospital.yadmNm,
        amount: 100,
        buyer_email: "user@example.com",
        buyer_name: "사용자 이름",
      },
      function (rsp) {
        if (rsp.success) {
          const detailPayment = {
            impUid: rsp.imp_uid,
            amount: rsp.paid_amount,
            hospId: selectedHospital.hospId,
            reservationAt: convertDate(selectedDate),
            reservationTime: selectedTime,
            redisKey: redisKey,
            providerId: providerId.id,
          };

          axios
            .post(
              `${process.env.REACT_APP_API_URL}/v1/payment`,
              detailPayment,
              {
                headers: { Authorization: `Bearer ${token}` },
                withCredentials: true,
              }
            )
            .then((response) => {
              showAlert("예약이 완료되었습니다.", "success");
              setTimeout(() => {
                navigate("/hospitalReservationHistory");
              }, 2000);
            })
            .catch(() => {
              showAlert("예약 처리 중 오류가 발생했습니다.", "error");
            });
        } else {
          axios.post(
            `${process.env.REACT_APP_API_URL}/v1/payment/redisremove`,
            redisKey,
            {
              headers: {
                "Content-Type": "text/plain",
              },
            }
          );
          showAlert("결제가 취소되었습니다.", "warning");
        }
      }
    );
  };

  return (
    <>
      {alert.show && <CustomAlert message={alert.message} type={alert.type} />}
      {showConfirm && (
        <CustomConfirm
          message={`${
            selectedDate.toISOString().split("T")[0]
          } ${selectedTime}에 예약하시겠습니까?`}
          onConfirm={handleConfirm}
          onCancel={() => setShowConfirm(false)}
        />
      )}
      <div className="modal-overlay" onClick={onClose}>
        <div className="modal-content" onClick={(e) => e.stopPropagation()}>
          <h2>날짜와 시간을 선택해 주세요</h2>

          <div className="datepicker-container">
            <DatePicker
              selected={selectedDate}
              onChange={(date) => {
                const offsetDate = new Date(
                  date.getTime() - date.getTimezoneOffset() * 60000
                );
                const formattedDate = offsetDate.toISOString().split("T")[0];
                getReservation(formattedDate);
                setSelectedDate(date);
                setSelectedTime(null);
              }}
              inline
              minDate={new Date()}
              locale={ko}
              dateFormat="yyyy년 MM월 dd일"
            />
          </div>

          {selectedDate && (
            <div className="timepicker-container">
              <h3>시간 선택</h3>
              <div className="time-slots">
                {availableTimes.map((time) => (
                  <button
                    key={time}
                    className={`time-slot-button ${
                      selectedTime === time ? "selected" : ""
                    } ${
                      reservationTimes.includes(time) || isTimeDisabled(time)
                        ? "disabled"
                        : ""
                    }`}
                    onClick={() =>
                      !reservationTimes.includes(time) &&
                      !isTimeDisabled(time) &&
                      setSelectedTime(time)
                    }
                    disabled={
                      reservationTimes.includes(time) || isTimeDisabled(time)
                    }
                  >
                    {time}
                  </button>
                ))}
              </div>
            </div>
          )}

          <div className="button-group">
            <button onClick={onClose}>닫기</button>
            <button onClick={handleReservation}>예약하기</button>
          </div>
        </div>
      </div>
    </>
  );
};

export default ReservationModal;
