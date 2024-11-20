import React, { useEffect, useState } from "react";
import axios from "axios";
import ReviewModal from "./ReviewModal";
import ReservationUpdateModal from "./ReservationUpdateModal";
import RefundModal from "./RefundModal";
import ReviewUpdateModal from "./ReviewUpdateModal";
import { jwtDecode } from "jwt-decode";
import "./HospitalReservationHistory.css";

function HospitalReservationHistory() {
  const [reservationList, setReservationList] = useState({
    number: 0,
    totalPage: 0,
    previous: false,
    next: false,
    hospitalReservationList: [],
  });
  const [selectReviewModal, setSelectReviewModal] = useState(false);
  const [selectUpdateModal, setSelectUpdateModal] = useState(false);
  const [selectRefundModal, setSelectRefundModal] = useState(false);
  const [selectReviewUpdateModal, setSelectReviewUpdateModal] = useState(false);
  const [reservation, setReservation] = useState(null);
  const token = localStorage.getItem("token");
  const providerId = jwtDecode(token).userId;
  const userData = {
    token: token,
    providerId: providerId,
  };

  useEffect(() => {
    myReservation();
  }, []);

  const myReservation = (type) => {
    axios
      .get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreservation/list?type=${type}&providerId=${providerId}`,
        {
          headers: { Authorization: `Bearer ${token}` }, // 인증 헤더 추가
          withCredentials: true, // CORS 인증 설정
        }
      )
      .then((response) => {
        console.log(response);
        setReservationList(response.data.data);
      });
  };
  const handleReviewModalClick = (reservation) => {
    setSelectReviewModal(true);
    setReservation(reservation);
  };

  const handleReservationUpdateModalClick = (reservation) => {
    setSelectUpdateModal(true);
    setReservation(reservation);
  };

  const handleReservationRefundModalClick = (reservation) => {
    setSelectRefundModal(true);
    setReservation(reservation);
  };
  const handleReviewUpdateModalClick = (reservation) => {
    setSelectReviewUpdateModal(true);
    setReservation(reservation);
  };

  const pageNums = [];
  const pageNum = (Math.floor((reservationList.number - 1) / 5) + 1) * 5;
  if (reservationList.totalPage !== 0) {
    if (reservationList.totalPage > pageNum) {
      for (let i = pageNum - 4; i <= pageNum; i++) {
        pageNums.push(i);
      }
    } else {
      for (let i = pageNum - 4; i <= reservationList.totalPage; i++) {
        pageNums.push(i);
      }
    }
  }
  const onReservation = (type) => {
    console.log("on");
    searchReservation(type);
  };

  const searchReservation = (type) => {
    axios
      .get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreservation/list?type=${type}&providerId=${providerId}`,
        {
          headers: { Authorization: `Bearer ${token}` }, // 인증 헤더 추가
          withCredentials: true, // CORS 인증 설정
        }
      )
      .then((response) => {
        setReservationList(response.data.data);
      });
  };

  return (
    <div>
      <div>
        <ul style={{ listStyleType: "none", padding: 0 }}>
          <li onClick={() => onReservation()}>예약 전체</li>
          <li onClick={() => onReservation("1")}>현재 진행중인 예약</li>
          <li onClick={() => onReservation("2")}>종료된 예약</li>
          <li onClick={() => onReservation("3")}>취소한 예약</li>
        </ul>
      </div>
      <div>
        <div>예약 내역</div>
        <div className="container mt-4">
          <div className="hospitalContainer">
            {reservationList.hospitalReservationList.map((item) => (
              <div className="hospitalList">
                <div className="hospitalInfo">
                  <ul style={{ listStyleType: "none", padding: 0 }}>
                    <li className="highlight">{item.yadmNm}</li>
                    <li
                      style={{ fontSize: "0.9em", color: "rgba(0, 0, 0, 0.6)" }}
                    >
                      {item.addr}
                    </li>
                    <li
                      style={{ fontSize: "0.9em", color: "rgba(0, 0, 0, 0.6)" }}
                    >
                      {item.telno}
                    </li>
                    <div>
                      <span>
                        예약시간 {item.reservationAt} {item.reservationTime}
                      </span>

                      {item.reservationStatus === "PENDING" ? (
                        <div className="button-container">
                          <button
                            onClick={() =>
                              handleReservationUpdateModalClick(item)
                            }
                          >
                            예약변경
                          </button>
                          <button
                            onClick={() =>
                              handleReservationRefundModalClick(item)
                            }
                          >
                            환불하기
                          </button>
                        </div>
                      ) : null}
                      {item.reservationStatus === "SUCCESS" ? (
                        <div>
                          <button onClick={() => handleReviewModalClick(item)}>
                            리뷰작성
                          </button>
                        </div>
                      ) : null}
                      {item.reservationStatus === "END" ? (
                        <div>
                          <button
                            onClick={() => handleReviewUpdateModalClick(item)}
                          >
                            리뷰수정
                          </button>
                        </div>
                      ) : null}
                      {item.reservationStatus === "CANCEL" ? (
                        <div>
                          <p>결제 취소</p>
                        </div>
                      ) : null}
                    </div>
                  </ul>
                </div>
                <div className="hospitalImage">
                  <img
                    src={`/hospitalImage/hosp${
                      Math.floor(Math.random() * 18) + 1
                    }.jpg`}
                    alt="Hospital"
                  />
                </div>
              </div>
            ))}
          </div>
          <div className="pagenav">
            {reservationList.previous ? (
              <button
              // onClick={() => onClickSelectPage(hospitalPages.number - 1)}
              >
                이전
              </button>
            ) : null}
            {pageNums.map((item) => (
              <button
                key={item}
                // onClick={() => onClickSelectPage(item)}
                style={{
                  backgroundColor:
                    reservationList.number === item ? "#007bff" : "transparent", // 현재 페이지일 경우 강조
                  color: reservationList.number === item ? "white" : "blue", // 강조된 텍스트 색상
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
            {reservationList.next ? (
              <button
              // onClick={() => onClickSelectPage(hospitalPages.number + 1)}
              >
                다음
              </button>
            ) : null}
          </div>
        </div>
        <ReviewModal
          isOpen={selectReviewModal}
          onClose={() => setSelectReviewModal(false)}
          hostReservation={reservation}
          userData={userData}
        />
        <ReservationUpdateModal
          isOpen={selectUpdateModal}
          onClose={() => setSelectUpdateModal(false)}
          hostReservation={reservation}
        />
        <RefundModal
          isOpen={selectRefundModal}
          onClose={() => setSelectRefundModal(false)}
          hostReservation={reservation}
          userData={userData}
        />
        <ReviewUpdateModal
          isOpen={selectReviewUpdateModal}
          onClose={() => setSelectReviewUpdateModal(false)}
          hostReservation={reservation}
        />
      </div>
    </div>
  );
}

export default HospitalReservationHistory;
