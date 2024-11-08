import React from "react";
import "./ReservationModal.css";

const ReservationModal = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        {/* 모달 내용 */}
        {children}
        <button onClick={onClose}>닫기</button>
      </div>
    </div>
  );
};

export default ReservationModal;
