import axios from "axios";
import React, { useState, useEffect } from "react";
import "./ReviewUpdateModal.css";

const ReviewUpdateModal = ({ isOpen, onClose, hostReservation }) => {
  const [rating, setRating] = useState(1);
  const [review, setReview] = useState("");

  useEffect(() => {
    if (isOpen) {
      getMyReview();
    }
  }, [isOpen]);

  const handleRatingClick = (value) => {
    setRating(value);
  };

  const getMyReview = () => {
    axios
      .get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreview?hospitalReviewId=${hostReservation.hospReviewId}`
      )
      .then((response) => {
        if (response.data.success) {
          setReview(response.data.data.hospReviewContent);
          setRating(response.data.data.hospReviewRating);
        }
      });
  };

  const handleReviewChange = (e) => {
    setReview(e.target.value);
  };

  const handleSubmit = () => {
    const hospitalReviewUpdateInput = {
      hospReviewId: hostReservation.hospReviewId,
      content: review,
      rating: rating,
    };
    axios
      .post(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreview/update`,
        hospitalReviewUpdateInput
      )
      .then((response) => {
        if (response.data.data) {
          console.log("respinse ==>> " + response.data.data);
          onClose();
          window.location.reload();
          // 성공 피드백을 추가할 수 있습니다
        }
      });
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>리뷰 수정</h2>
        <p>
          <span className="hospital-name">{hostReservation?.yadmNm}</span>에
          대한 평점과 리뷰를 수정해주세요.
        </p>

        <div className="rating-container">
          {Array.from({ length: 5 }, (_, index) => {
            const starValue = index + 1;
            return (
              <span
                key={index}
                className={`star ${
                  starValue <= rating ? "active" : "inactive"
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

        <div className="button-group">
          <button className="button-base cancel-button" onClick={onClose}>
            닫기
          </button>
          <button className="button-base submit-button" onClick={handleSubmit}>
            수정
          </button>
        </div>
      </div>
    </div>
  );
};

export default ReviewUpdateModal;
