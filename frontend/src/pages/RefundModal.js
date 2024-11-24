import axios from "axios";
import React, { useState } from "react";

const RefundModal = ({ isOpen, onClose, hostReservation }) => {
  if (!isOpen) return null; // 모달이 열리지 않으면 렌더링하지 않음

  const refund = (hostReservation) => {
    console.log("병원 결제 아이디", hostReservation.hospReservationId);
    const reservation = {
      reservationId: hostReservation.hospReservationId,
    };
    axios
      .post(`${process.env.REACT_APP_API_URL}/v1/payment/refund`, reservation)
      .then((response) => {
        if (response.data.data) {
          // 환불 성공
          console.log("환불에 성공했습니다.");
        } else {
          // 환불 실패
        }
      });
  };

  return (
    <div style={modalOverlayStyle} onClick={onClose}>
      <div style={modalContentStyle} onClick={(e) => e.stopPropagation()}>
        <h2>환불 요청</h2>
        {hostReservation && (
          <p>"{hostReservation.yadmNm}" 예약에 대해 환불을 진행하시겠습니까?</p>
        )}
        <div style={{ marginTop: "20px" }}>
          <button
            style={{
              ...buttonStyle,
              backgroundColor: "red",
              marginLeft: "10px",
            }}
            onClick={() => refund(hostReservation)}
          >
            환불 요청
          </button>
          <button style={buttonStyle} onClick={onClose}>
            닫기
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

const buttonStyle = {
  padding: "10px 20px",
  backgroundColor: "#007BFF",
  color: "white",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
};

export default RefundModal;
