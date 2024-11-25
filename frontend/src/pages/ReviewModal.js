import axios from "axios";
import React, { useState } from "react";
import "./ReviewModal.css";

const ReviewModal = ({ isOpen, onClose, hostReservation, userData }) => {
  const [rating, setRating] = useState(1);
  const [review, setReview] = useState("");

  if (!isOpen) return null;

  const handleRatingClick = (value) => {
    setRating(value);
  };

  const handleReviewChange = (e) => {
    setReview(e.target.value);
  };

  const handleSubmit = () => {
    const hospitalReviewInput = {
      content: review,
      rating: rating,
      hospitalReservationId: hostReservation.hospReservationId,
      hospId: hostReservation.hospId,
      providerId: userData.providerId,
    };

    axios
      .post(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreview/save`,
        hospitalReviewInput,
        {
          headers: { Authorization: `Bearer ${userData.token}` },
          withCredentials: true,
        }
      )
      .then((response) => {
        onClose();
        window.location.reload();
      })
      .catch((error) => {
        // 에러 처리를 추가할 수 있습니다
      });
  };

  return (
    <div className="review-modal-overlay">
      <div className="review-modal-content">
        <h2 className="review-modal-title">리뷰 작성</h2>
        <p className="review-modal-subtitle">
          <span className="review-hospital-name">
            {hostReservation?.yadmNm}
          </span>
          에 대한 평점과 리뷰를 작성해주세요.
        </p>

        <div className="rating-stars-container">
          {Array.from({ length: 5 }, (_, index) => {
            const starValue = index + 1;
            return (
              <span
                key={index}
                className={`rating-star ${
                  starValue <= rating ? "filled" : "empty"
                }`}
                onClick={() => handleRatingClick(starValue)}
              >
                ★
              </span>
            );
          })}
        </div>
        <p className="rating-text">선택된 평점: {rating}점</p>

        <textarea
          className="review-textarea"
          placeholder={`${hostReservation?.yadmNm}에 대한 리뷰를 작성해주세요.`}
          value={review}
          onChange={handleReviewChange}
        />

        <div className="button-container">
          <button className="button cancel-button" onClick={onClose}>
            취소
          </button>
          <button className="button submit-button" onClick={handleSubmit}>
            작성
          </button>
        </div>
      </div>
    </div>
  );
};

export default ReviewModal;
