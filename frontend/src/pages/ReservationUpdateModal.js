import React, { useState } from "react";
import axios from "axios";
import DatePicker from "react-datepicker";
import { ko } from "date-fns/locale";
import "react-datepicker/dist/react-datepicker.css";

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
    await axios
      .get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreservation?reservationAt=${formattedDate}&hospId=${hostReservation.hospId}`
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
      alert("예약 날짜와 시간을 선택해주세요.");
      return;
    }
    console.log(hospitalReservation.hospReservationId);
    const reservationUpdateRequest = {
      reservationId: hospitalReservation.hospReservationId,
      hospId: hospitalReservation.hospId,
      reservationAt: selectedDate,
      reservationTime: selectedTime,
    };
    if (
      window.confirm(
        selectedDate + ":" + selectedTime + "로 예약을 변경하시 겠습니까??"
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
        });
    } else {
      alert("예약 변경이 취소 되었습니다. 다시 한번 부탁드립니다.");
    }
  };

  if (!isOpen) return null; // 모달이 열리지 않았으면 렌더링하지 않음

  return (
    <div style={modalOverlayStyle} onClick={onClose}>
      <div style={modalContentStyle} onClick={(e) => e.stopPropagation()}>
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

        {/* Step 1: 날짜 선택 */}
        <div>
          <h3>날짜 선택</h3>
          <DatePicker
            selected={selectedDate}
            onChange={handleDateChange}
            inline
            minDate={new Date()} // 오늘 이후만 선택 가능
            locale={ko}
            dateFormat="yyyy년 MM월 dd일"
          />
        </div>

        {/* Step 2: 시간 선택 */}
        {selectedDate && (
          <div>
            <h3>시간 선택</h3>
            <div style={timeSlotContainerStyle}>
              {availableTimes.map((time) => (
                <button
                  key={time}
                  style={{
                    ...timeSlotButtonStyle,
                    ...(selectedTime === time ? selectedStyle : {}),
                    ...(reservationTimes.includes(time) ? disabledStyle : {}),
                  }}
                  onClick={() => handleTimeClick(time)}
                  disabled={reservationTimes.includes(time)}
                >
                  {time}
                </button>
              ))}
            </div>
          </div>
        )}

        <div style={buttonGroupStyle}>
          <button onClick={onClose} style={closeButtonStyle}>
            닫기
          </button>
          <button
            onClick={() => updateReservation(hostReservation)}
            style={saveButtonStyle}
          >
            예약 변경
          </button>
        </div>
      </div>
    </div>
  );
};

// 스타일
const modalOverlayStyle = {
  position: "fixed",
  top: 0,
  left: 0,
  width: "100%",
  height: "100%",
  backgroundColor: "rgba(0, 0, 0, 0.5)",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  zIndex: 1000,
};

const modalContentStyle = {
  background: "white",
  padding: "20px",
  borderRadius: "8px",
  textAlign: "center",
  width: "400px",
};

const timeSlotContainerStyle = {
  display: "flex",
  flexWrap: "wrap",
  justifyContent: "center",
  gap: "10px",
  margin: "10px 0",
};

const timeSlotButtonStyle = {
  padding: "10px",
  border: "1px solid #ccc",
  borderRadius: "4px",
  cursor: "pointer",
};

const selectedStyle = {
  backgroundColor: "#007BFF",
  color: "white",
};

const disabledStyle = {
  backgroundColor: "#ccc",
  color: "white",
  cursor: "not-allowed",
};

const buttonGroupStyle = {
  marginTop: "20px",
  display: "flex",
  justifyContent: "space-around",
};

const closeButtonStyle = {
  padding: "10px 20px",
  backgroundColor: "#6c757d",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

const saveButtonStyle = {
  padding: "10px 20px",
  backgroundColor: "#007BFF",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

export default ReservationUpdateModal;
