import React, { useState } from "react";
import axios from "axios";
import DatePicker from "react-datepicker";
import { ko } from "date-fns/locale";
import "react-datepicker/dist/react-datepicker.css";
import "./ReservationUpdateModal.css"; // CSS 파일 임포트

const ReservationUpdateModal = ({ isOpen, onClose, hostReservation }) => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState(null);
  const [reservationTimes, setReservationTimes] = useState([]);

  // 예약 가능한 시간대
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
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreservation?reservationAt=${formattedDate}&hospId=${hostReservation.hospId}`
      );
      if (response.data.success) {
        const newReservationTime = [];
        response.data.data.forEach((item) => {
          newReservationTime.push(item.reservationTime);
        });
        setReservationTimes([...newReservationTime]);
      }
    } catch (error) {
      console.error("예약 정보를 가져오는 데 실패했습니다.", error);
      window.alert("예약 정보를 불러오는 데 실패했습니다.");
    }
  };

  const handleDateChange = (date) => {
    const offsetDate = new Date(
      date.getTime() - date.getTimezoneOffset() * 60000
    );
    const formattedDate = offsetDate.toISOString().split("T")[0];

    setSelectedDate(formattedDate);
    setSelectedTime(null);
    getReservation(formattedDate);
  };

  const handleTimeClick = (time) => {
    if (!reservationTimes.includes(time)) {
      setSelectedTime(time); // 선택한 시간 설정
    }
  };

  const updateReservation = (hospitalReservation) => {
    if (!selectedDate || !selectedTime) {
      window.alert("예약 날짜와 시간을 선택해주세요.");
      return;
    }

    const reservationUpdateRequest = {
      reservationId: hospitalReservation.hospReservationId,
      hospId: hospitalReservation.hospId,
      reservationAt: selectedDate,
      reservationTime: selectedTime,
    };

    if (
      window.confirm(
        `${selectedDate} ${selectedTime}로 예약을 변경하시겠습니까?`
      )
    ) {
      // 확인을 누른 경우 실행할 코드
      axios
        .post(
          `${process.env.REACT_APP_API_URL}/v1/hospitalreservation/update`,
          reservationUpdateRequest
        )
        .then((response) => {
          if (response.data.data) {
            onClose(); // 모달 닫기
            window.location.reload();
          }
        })
        .catch((error) => {
          console.error("예약 변경에 실패했습니다.", error);
          window.alert("예약 변경에 실패했습니다. 다시 시도해주세요.");
        });
    } else {
      window.alert("예약 변경이 취소되었습니다.");
    }
  };

  if (!isOpen) return null; // 모달이 열리지 않았으면 렌더링하지 않음

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <h2>예약 변경</h2>
        {hostReservation && (
          <>
            <p>병원 이름: {hostReservation.yadmNm}</p>
            <p>주소: {hostReservation.addr}</p>
            <p>
              예약: {hostReservation.reservationAt}:{" "}
              {hostReservation.reservationTime}
            </p>
          </>
        )}

        {/* 날짜 선택 */}
        <div>
          <h3>날짜 선택</h3>
          <DatePicker
            selected={selectedDate ? new Date(selectedDate) : null}
            onChange={handleDateChange}
            inline
            minDate={new Date()} // 오늘 이후만 선택 가능
            locale={ko}
            dateFormat="yyyy년 MM월 dd일"
          />
        </div>

        {/* 시간 선택 */}
        {selectedDate && (
          <div>
            <h3>시간 선택</h3>
            <div className="time-slot-container">
              {availableTimes.map((time) => (
                <button
                  key={time}
                  className={`time-slot-button ${
                    selectedTime === time ? "selected" : ""
                  } ${reservationTimes.includes(time) ? "disabled" : ""}`}
                  onClick={() => handleTimeClick(time)}
                  disabled={reservationTimes.includes(time)}
                >
                  {time}
                </button>
              ))}
            </div>
          </div>
        )}

        <div className="button-group">
          <button onClick={onClose} className="button-base cancel-button">
            닫기
          </button>
          <button
            onClick={() => updateReservation(hostReservation)}
            className="button-base save-button"
          >
            예약 변경
          </button>
        </div>
      </div>
    </div>
  );
};

export default ReservationUpdateModal;
