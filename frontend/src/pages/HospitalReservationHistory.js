import React, { useEffect, useState } from "react";
import axios from "axios";
import ReviewModal from "./ReviewModal";
import ReservationUpdateModal from "./ReservationUpdateModal";
import RefundModal from "./RefundModal";
import ReviewUpdateModal from "./ReviewUpdateModal";
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

  useEffect(() => {
    myReservation(1);
  }, []);

  const myReservation = (type) => {
    axios
      .get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreservation/list?type=${type}&userId=2`
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
  const cancelReservation = (type) => {
    console.log("cancel");
    searchReservation(type);
  };
  const endReservation = (type) => {
    console.log("end");
    searchReservation(type);
  };
  const searchReservation = (type) => {
    axios
      .get(
        `${process.env.REACT_APP_API_URL}/v1/hospitalreservation/list?type=${type}`
      )
      .then((response) => {
        setReservationList(response.data.data);
      });
  };

  return (
    <div>
      <div>
        <ul style={{ listStyleType: "none", padding: 0 }}>
          <li onClick={() => onReservation("1")}>현재 진행중인 예약</li>
          <li onClick={() => cancelReservation("2")}>종료된 예약</li>
          <li onClick={() => endReservation("3")}>취소한 예약</li>
        </ul>
      </div>
      <div>
        <div>예약 내역</div>
        <div className="container mt-4">
          {reservationList.hospitalReservationList.map((item) => (
            <ul style={{ listStyleType: "none", padding: 0 }}>
              <li>{item.hospReservationId}</li>
              <li>{item.yadmNm}</li>
              <li>{item.addr}</li>
              <li>{item.telno}</li>
              <div>
                <span>
                  예약시간 {item.reservationAt} {item.reservationTime}
                </span>

                {item.reservationStatus === "PENDING" ? (
                  <div>
                    <button
                      onClick={() => handleReservationUpdateModalClick(item)}
                    >
                      예약변경
                    </button>
                    <button
                      onClick={() => handleReservationRefundModalClick(item)}
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
                    <button onClick={() => handleReviewUpdateModalClick(item)}>
                      리뷰수정
                    </button>
                  </div>
                ) : null}
              </div>
            </ul>
          ))}
          <div>
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
                  background: "none",
                  border: "none",
                  color: "blue",
                  cursor: "pointer",
                  textDecoration: "underline",
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
