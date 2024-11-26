import React, { useEffect, useState, memo } from "react";
import axios from "axios";
import Location from "./Location";
import ReservationModal from "./ReservationModal";
import { jwtDecode } from "jwt-decode";
import "./Dashboard.css";
import ReviewListModal from "./ReviewListModal";
import { StarIcon } from "@heroicons/react/solid";

function Dashboard() {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달창 열때
  const [selectedHospital, setSelectedHospital] = useState(null); // 모달창 열었을때 정보
  const token = localStorage.getItem("token");
  const providerId = jwtDecode(token).userId;
  const [selectedReviewModalOpen, setSelectedReviewModalOpen] = useState(null);

  const findUserInfo = () => {
    axios
      .get(`${process.env.REACT_APP_API_URL}/v1/user/${providerId}`, {
        headers: { Authorization: `Bearer ${token}` }, // 인증 헤더 추가
        withCredentials: true, // CORS 인증 설정
      })
      .then((response) => {
        if (response.data.success) {
          // setSearch({ ...search, sidoCdNm: "경기" }); 여기서 회원 주소 입력
        }
      });
  };


  const [hospitalPages, setHospitalPages] = useState({
    number: 0,
    totalPage: 0,
    previous: false,
    next: false,
    hospitals: [],
  });

  const [search, setSearch] = useState({
    sidoCdNm: "",
    sgguCdNm: "",
    keyWord: "",
    page: 1,
  });

  const [searchInput, setSearchInput] = useState({
    sidoCdNm: "",
    sgguCdNm: "",
    keyWord: "",
    page: 1,
  });

  const onChangeSearchLocation = (location) => {
    setSearchInput({ ...searchInput, ...location });
  };

  const getHospitals = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/hospital/search?sidoCdNm=${search.sidoCdNm}&sgguCdNm=${search.sgguCdNm}&keyWord=${search.keyWord}&page=${search.page}`
      );
      if (response.data.success) {
        setHospitalPages({ ...response.data.data });
      }
    } catch (error) {
      console.error("API 요청 오류:", error);
    }
  };

  const onClickSearch = () => {
    setSearch({ ...searchInput });
  };
  useEffect(() => {
    findUserInfo();
  }, []);

  useEffect(() => {
    getHospitals();
  }, [search]);

  const onChangeKeyWord = (e) => {
    setSearchInput({ ...searchInput, keyWord: e.target.value });
  };

  const onClickSelectPage = (page) => {
    setSearch({ ...search, page: page });
  };

  const pageNums = [];
  const pageNum = (Math.floor((hospitalPages.number - 1) / 5) + 1) * 5;
  if (hospitalPages.totalPage !== 0) {
    if (hospitalPages.totalPage > pageNum) {
      for (let i = pageNum - 4; i <= pageNum; i++) {
        pageNums.push(i);
      }
    } else {
      for (let i = pageNum - 4; i <= hospitalPages.totalPage; i++) {
        pageNums.push(i);
      }
    }
  }

  const handleReservationClick = (hospital) => {
    setIsModalOpen(true);
    setSelectedHospital(hospital);
  };

  const openReview = (hospital) => {
    console.log("리뷰보기");
    setSelectedReviewModalOpen(true);
    setSelectedHospital(hospital);
  };

  return (
    <div className="card shadow-sm p-4" style={{ width: "100%" }}>
      <h3 className="text-center mb-4">병원목록</h3>
      <div className="searchBox">
        <div className="searchBar">
          <input
            className="keyword"
            type="text"
            id=""
            value={searchInput.keyWord}
            onChange={onChangeKeyWord}
          />
          <button className="searchButton" onClick={onClickSearch}>
            병원 조회
          </button>
        </div>
        <Location onChangeSearchLocation={onChangeSearchLocation} />
      </div>
      <div className="container mt-4">
        <div className="hospitalContainer">
          {hospitalPages.hospitals.map((item) => (
            <div className="hospitalList" onClick={() => openReview(item)}>
              <div className="hospitalInfo">
                <ul style={{ listStyleType: "none", padding: 0, margin: 0 }}>
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
                  <div className="info-row">
                    {item.reviewCount === 0 ? (
                      <div className="info-details">
                        <div className="flex-row">
                          <StarIcon
                            style={{ width: "15px", height: "15px" }}
                            className="text-yellow-500"
                          />
                          <span className="no-wrap">수집중</span>
                        </div>
                        <button
                          className="no-wrap"
                          onClick={(e) => {
                            e.stopPropagation();
                            handleReservationClick(item);
                          }}
                        >
                          예약하기
                        </button>
                      </div>
                    ) : (
                      <div className="info-details">
                        <span className="star">
                          <StarIcon
                            style={{ width: "15px", height: "15px" }}
                            className="text-yellow-500"
                          />
                          {item.ratingAvg}
                        </span>
                        &nbsp;
                        <span className="no-wrap">리뷰 {item.reviewCount}</span>
                        <button
                          className="no-wrap"
                          style={{ marginLeft: "6px" }}
                          onClick={(e) => {
                            e.stopPropagation();
                            handleReservationClick(item);
                          }}
                        >
                          예약하기
                        </button>
                      </div>
                    )}
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
          {hospitalPages.previous ? (
            <button onClick={() => onClickSelectPage(hospitalPages.number - 1)}>
              이전
            </button>
          ) : null}
          {pageNums.map((item) => (
            <button
              key={item}
              onClick={() => onClickSelectPage(item)}
              style={{
                backgroundColor:
                  hospitalPages.number === item ? "#007bff" : "transparent", // 현재 페이지일 경우 강조
                color: hospitalPages.number === item ? "white" : "blue", // 강조된 텍스트 색상
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
          {hospitalPages.next ? (
            <button onClick={() => onClickSelectPage(hospitalPages.number + 1)}>
              다음
            </button>
          ) : null}
        </div>
      </div>
      <ReservationModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        selectedHospital={selectedHospital}
      ></ReservationModal>
      <ReviewListModal
        isOpen={selectedReviewModalOpen}
        onClose={() => setSelectedReviewModalOpen(false)}
        hospital={selectedHospital}
      />
    </div>
  );
}

export default Dashboard;
