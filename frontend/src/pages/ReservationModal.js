// ReservationModal.js
import React, { useState, useEffect } from "react";
import axios from "axios";
import DatePicker from "react-datepicker";
import { ko } from "date-fns/locale";
import "react-datepicker/dist/react-datepicker.css";
import "./ReservationModal.css";
import { useNavigate } from "react-router-dom";
import ReviewModal from "./ReviewModal";

const ReservationModal = ({ isOpen, onClose, selectedHospital }) => {
  const navigate = useNavigate();
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState(null);
  const [reservationTimes, setReservationTimes] = useState([]);
  const convertDate = (date) => {
    const offsetDate = new Date(
      date.getTime() - date.getTimezoneOffset() * 60000
    );
    const formattedDate = offsetDate.toISOString().split("T")[0];
    return formattedDate;
  };
  let redisKey = "";
  if (!isOpen) return null;

  // 예약 가능한 시간대를 설정합니다 (9:00부터 17:30까지 30분 단위로 설정, 단 12:00부터 13:00시는 제외)
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
  const getReservation = async (formattedDate) => {
    await axios
      .get(
        `http://localhost:8080/api/v1/hospitalreservation?reservationAt=${formattedDate}&hospId=${selectedHospital.hospId}`
      )
      .then((response) => {
        if (response.data.success) {
          const newReservationTime = [];
          response.data.data.forEach((item) => {
            newReservationTime.push(item.reservationTime);
          });
          setReservationTimes([...newReservationTime]);
        }
      });
  };

  const handleReservation = () => {
    if (!selectedDate || !selectedTime) {
      alert("예약 날짜와 시간을 선택해주세요.");
      return;
    }

    const reservationData = {
      hospitalId: selectedHospital.hospId,
      date: `${selectedDate.toISOString().split("T")[0]} ${selectedTime}`,
    };

    console.log(reservationData);
    if (window.confirm(reservationData.date + " 결제하시겠습니까?")) {
      // 확인을 누른 경우 실행할 코드
      console.log("결제를 진행합니다.");
      createReservation();

      // 결제 함수 호출 또는 결제 관련 처리
    } else {
      // 취소를 누른 경우 실행할 코드
      console.log("결제를 취소합니다.");
      // 취소 관련 처리
    }
  };
  const createReservation = () => {
    const selctConvertDate = convertDate(selectedDate);
    const resercationData = {
      hospId: selectedHospital.hospId,
      reservationAt: selctConvertDate,
      reservationTime: selectedTime,
    };
    axios
      .post(`http://localhost:8080/api/v1/hospitalreservation`, resercationData)
      .then((response) => {
        if (!response.data.data.isvalid) {
          // 중복되는 예약이 없다.
          redisKey = response.data.data.redisKey;
          requestPay(redisKey);
        } else {
          alert("결제에 실패했습니다. 다시 부탁드립니다.", redisKey);
        }
      })
      .catch((error) => {
        console.error("예약 실패:???", error);
        // 에러 처리 코드
      });
  };
  function requestPay(redisKey) {
    if (!window.IMP) {
      console.error("아임포트 라이브러리가 로드되지 않았습니다.");
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
          };

          axios
            .post(`http://localhost:8080/api/v1/payment`, detailPayment)
            .then((response) => {
              console.log(response);
              navigate("/hospitalReservationHistory");
            });
        } else {
          axios
            .post(
              `http://localhost:8080/api/v1/payment/redisremove`,
              redisKey,
              {
                headers: {
                  "Content-Type": "text/plain",
                },
              }
            )
            .then((response) => {});
          console.error("결제 실패", rsp.error_msg);
        }
      }
    );
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <h2>{selectedHospital.yadmNm} 예약하기</h2>

        {/* Step 1: Date Selection */}
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
              setSelectedTime(null); // Reset the time selection when the date changes
            }}
            inline
            minDate={new Date()} // 오늘 이후 날짜만 선택 가능
            locale={ko}
            dateFormat="yyyy년 MM월 dd일"
          />
        </div>

        {/* Step 2: Time Selection (only if date is selected) */}
        {selectedDate && (
          <div className="timepicker-container">
            <h3>시간 선택</h3>
            <div className="time-slots">
              {availableTimes.map((time) => (
                <button
                  key={time}
                  className={`time-slot-button ${
                    selectedTime === time ? "selected" : ""
                  } ${reservationTimes.includes(time) ? "disabled" : ""}`}
                  onClick={() =>
                    !reservationTimes.includes(time) && setSelectedTime(time)
                  }
                  disabled={reservationTimes.includes(time)}
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
  );
};

export default ReservationModal;
