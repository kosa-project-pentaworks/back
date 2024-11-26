import React, {useEffect, useState, memo} from "react";
import axios from "axios";
import Location from "./Location";
import ReservationModal from "./ReservationModal";
import "./Dashboard.css";
import ReviewListModal from "./ReviewListModal";
import {StarIcon} from "@heroicons/react/solid";

function Main() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedHospital, setSelectedHospital] = useState(null);

    const [selectedReviewModalOpen, setSelectedReviewModalOpen] = useState(null);
    const [img, setImg] = useState([]);

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
        setSearchInput({...searchInput, ...location});
    };

    const getHospitals = async () => {
        try {
            const response = await axios.get(
                `${process.env.REACT_APP_API_URL}/v1/hospital/search?sidoCdNm=${search.sidoCdNm}&sgguCdNm=${search.sgguCdNm}&keyWord=${search.keyWord}&page=${search.page}`
            );
            if (response.data.success) {
                setHospitalPages({...response.data.data});
            }
        } catch (error) {
            console.error("API 요청 오류:", error);
        }
    };

    const onClickSearch = () => {
        setSearch({...searchInput});
    };

    useEffect(() => {
        getHospitals();
    }, [search]);

    useEffect(() => {
        const arr = [];
        for (let i = 0; i < 10; i++) {
            arr.push(Math.floor(Math.random() * 18) + 1);
        }
        setImg(arr);
    }, [hospitalPages]);

    const onChangeKeyWord = (e) => {
        setSearchInput({...searchInput, keyWord: e.target.value});
    };

    const onClickSelectPage = (page) => {
        setSearch({...search, page: page});
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
        setSelectedReviewModalOpen(true);
        setSelectedHospital(hospital);
    };

    return (
        <div className="card shadow-sm p-4" style={{width: "100%"}}>
            <h3 className="text-center mb-4">병원목록</h3>
            <div className="searchBox">
                <div className="searchBar">
                    <input
                        className="keyword"
                        type="text"
                        value={searchInput.keyWord}
                        onChange={onChangeKeyWord}/>
                    <button className="searchButton" onClick={onClickSearch}>
                        병원 조회
                    </button>
                </div>
                <Location onChangeSearchLocation={onChangeSearchLocation}/>
            </div>
            <div className="container mt-4">
                <div className="hospitalContainer">
                    {hospitalPages.hospitals.map((item, idx) => (
                        <div
                            key={idx}
                            className="hospitalList"
                            onClick={() => openReview(item)}
                        >
                            <div className="hospitalInfo">
                                <ul style={{listStyleType: "none", padding: 0, margin: 0}}>
                                    <li className="highlight">{item.yadmNm}</li>
                                    <li className="hospital-details">{item.addr}</li>
                                    <li className="hospital-details">{item.telno}</li>
                                </ul>
                                {/* `div`를 `ul` 밖으로 이동 */}
                                <div className="info-row">
                                    <div className="info-details">
                                        <div className="rating-container">
                                            <div className="rating-box">
                                                <StarIcon
                                                    style={{width: "15px", height: "15px"}}
                                                    className="text-yellow-500"
                                                />
                                                {item.reviewCount === 0 ? (
                                                    <span>수집중</span>
                                                ) : (
                                                    <span>{item.ratingAvg}</span>
                                                )}
                                            </div>
                                            {item.reviewCount !== 0 && (
                                                <span className="no-wrap">리뷰 {item.reviewCount}</span>
                                            )}
                                        </div>
                                        <button
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleReservationClick(item);
                                            }}
                                        >
                                            예약하기
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div className="hospitalImage">
                                <img
                                    src={`/hospitalImage/hosp${img[idx]}.jpg`}
                                    alt="Hospital"
                                />
                            </div>
                        </div>
                    ))}
                </div>
                <div className="pagenav">
                    {hospitalPages.previous && (
                        <button onClick={() => onClickSelectPage(hospitalPages.number - 1)}>
                            이전
                        </button>
                    )}
                    {pageNums.map((item) => (
                        <button
                            key={item}
                            onClick={() => onClickSelectPage(item)}
                            className={hospitalPages.number === item ? "active" : ""}
                        >
                            {item}
                        </button>
                    ))}
                    {hospitalPages.next && (
                        <button onClick={() => onClickSelectPage(hospitalPages.number + 1)}>
                            다음
                        </button>
                    )}
                </div>
            </div>
            <ReservationModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                selectedHospital={selectedHospital}/>
            <ReviewListModal
                isOpen={selectedReviewModalOpen}
                onClose={() => setSelectedReviewModalOpen(false)}
                hospital={selectedHospital}/>
        </div>
    );
}

export default Main;
