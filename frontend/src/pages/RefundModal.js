import axios from "axios";
import React, { useState } from "react";
import "./RefundModal.css";

const CustomAlert = ({ message, type, onClose }) => {
  return (
    <div className="alert-overlay">
      <div className={`alert-container alert-${type}`}>
        <span className="alert-icon">{type === "success" ? "✓" : "✕"}</span>
        <span className="alert-message">{message}</span>
      </div>
    </div>
  );
};

const RefundModal = ({ isOpen, onClose, hostReservation }) => {
  const [alert, setAlert] = useState({ show: false, message: "", type: "" });

  if (!isOpen) return null;

  const showAlert = (message, type) => {
    setAlert({ show: true, message, type });
    setTimeout(() => {
      setAlert({ show: false, message: "", type: "" });
    }, 2000); // 2초 후 알림 닫기
  };

  const handleRefund = (hostReservation) => {
    const reservation = {
      reservationId: hostReservation.hospReservationId,
    };

    axios
      .post(`${process.env.REACT_APP_API_URL}/v1/payment/refund`, reservation)
      .then((response) => {
        if (response.data.data) {
          showAlert("환불이 성공적으로 처리되었습니다.", "success");
          setTimeout(() => {
            onClose();
            window.location.reload();
          }, 2000);
        } else {
          showAlert("환불 처리에 실패했습니다. 다시 시도해주세요.", "error");
        }
      })
      .catch((error) => {
        showAlert(
          "환불 처리 중 오류가 발생했습니다. 다시 시도해주세요.",
          "error"
        );
      });
  };

  return (
    <>
      {alert.show && (
        <CustomAlert
          message={alert.message}
          type={alert.type}
          onClose={() => setAlert({ show: false, message: "", type: "" })}
        />
      )}
      <div className="refund-modal-overlay" onClick={onClose}>
        <div
          className="refund-modal-content"
          onClick={(e) => e.stopPropagation()}
        >
          <h2 className="refund-modal-title">환불 요청</h2>
          {hostReservation && (
            <p className="refund-modal-message">
              <span className="hospital-name">{hostReservation.yadmNm}</span>
              <br />
              예약에 대해 환불을 진행하시겠습니까?
            </p>
          )}

          <div className="button-group">
            <button className="button cancel-button" onClick={onClose}>
              취소
            </button>
            <button
              className="button refund-button"
              onClick={() => handleRefund(hostReservation)}
            >
              환불하기
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default RefundModal;
