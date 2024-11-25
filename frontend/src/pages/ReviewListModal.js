import axios from "axios";
import React, { useEffect, useState } from "react";
import "./ReviewListModal.css";
function ReviewListModal({ isOpen, onClose, hospital }) {
  const [findReviews, setFindReviews] = useState({
    number: 0,
    totalPage: 0,
    previous: false,
    next: false,
    reviewList: [],
  });
  useEffect(() => {
    if (isOpen) {
      findReview();
    }
  }, [isOpen]);
  if (!isOpen) return null;

  const findReview = () => {
    axios
      .get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreview/find?hospId=${hospital.hospId}`
      )
      .then((response) => {
        setFindReviews({ ...response.data.data });
      });
  };

  const pageNums = [];
  const pageNum = (Math.floor((findReviews.number - 1) / 5) + 1) * 5;
  if (findReviews.totalPage !== 0) {
    if (findReviews.totalPage > pageNum) {
      for (let i = pageNum - 4; i <= pageNum; i++) {
        pageNums.push(i);
      }
    } else {
      for (let i = pageNum - 4; i <= findReviews.totalPage; i++) {
        pageNums.push(i);
      }
    }
  }
  const onClickPageMove = (page) => {
    axios
      .get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreview/find?hospId=${hospital.hospId}&page=${page}`
      )
      .then((response) => {
        console.log(response.data.data);
        setFindReviews({ ...response.data.data });
      });
  };
  if (hospital.reviewCount === 0) {
    onClose(); // 모달 닫기
    return null;
  }
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div
        className="modal-content"
        onClick={(e) => e.stopPropagation()} // 이벤트 전파 방지
      >
        <div>
          <div className="top">
            <p>{hospital.yadmNm} 리뷰</p>
            <button className="modal-close" onClick={onClose}>
              &times;
            </button>
          </div>
          {findReviews.reviewList.map((item) => (
            <div className="review-box" key={item.id}>
              <ul>
                <div className="title">
                  <li className="highlight">{item.username}</li>
                  <li
                    style={{ fontSize: "0.8em", color: "rgba(0, 0, 0, 0.6)" }}
                  >
                    방문 날짜: {item.reservationAt} {item.reservationTime}
                  </li>
                </div>
                {Array.from({ length: 5 }, (_, index) => {
                  const starValue = index + 1;
                  return (
                    <span
                      key={index}
                      className={`star ${
                        starValue <= item.hospReviewRating ? "filled" : "empty"
                      }`}
                    >
                      ★
                    </span>
                  );
                })}
                <li>{item.hospReviewContent}</li>
              </ul>
            </div>
          ))}
        </div>
        <div className="pagenav">
          {findReviews.previous ? (
            <button onClick={() => onClickPageMove(findReviews.number - 1)}>
              이전
            </button>
          ) : null}
          {pageNums.map((item) => (
            <button
              key={item}
              onClick={() => onClickPageMove(item)}
              style={{
                backgroundColor:
                  findReviews.number === item ? "#007bff" : "transparent", // 현재 페이지일 경우 강조
                color: findReviews.number === item ? "white" : "blue", // 강조된 텍스트 색상
                border: "1px solid #ddd",
                borderRadius: "4px",
                padding: "8px 12px",
                margin: "0 4px",
                cursor: "pointer",
              }}
            >
              {item}
            </button>
          ))}
          {findReviews.next ? (
            <button onClick={() => onClickPageMove(findReviews.number + 1)}>
              다음
            </button>
          ) : null}
        </div>
      </div>
    </div>
  );
}

export default ReviewListModal;
