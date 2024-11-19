import React, { useEffect, useState } from "react";
import axios from "axios";
import Location from "./Location";
import ReservationModal from "./ReservationModal";

function Dashboard() {
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달창 열때
  const [selectedHospital, setSelectedHospital] = useState(null); // 모달창 열었을때 정보

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
    console.log("새로고침");
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

  return (
    <div className="card shadow-sm p-4" style={{ width: "100%" }}>
      <h3 className="text-center mb-4">병원목록</h3>
      <input
        type="text"
        id=""
        value={searchInput.keyWord}
        onChange={onChangeKeyWord}
      />
      <Location onChangeSearchLocation={onChangeSearchLocation} />
      <button onClick={onClickSearch}>병원 조회</button>
      <div className="container mt-4">
        {hospitalPages.hospitals.map((item) => (
          <ul style={{ listStyleType: "none", padding: 0 }}>
            <li>{item.hospId}</li>
            <li>{item.yadmNm}</li>
            <li>{item.addr}</li>
            <li>{item.telno}</li>
            <div>
              <span>리뷰 {item.reviewCount}</span>
              <span>평점 {item.ratingAvg}</span>{" "}
              <button onClick={() => handleReservationClick(item)}>
                예약하기
              </button>
            </div>
          </ul>
        ))}
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
        {hospitalPages.next ? (
          <button onClick={() => onClickSelectPage(hospitalPages.number + 1)}>
            다음
          </button>
        ) : null}
      </div>
      <ReservationModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        selectedHospital={selectedHospital}
      ></ReservationModal>
    </div>
  );
}

export default Dashboard;
