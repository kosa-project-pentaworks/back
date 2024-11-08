import { useState, useEffect } from "react";

const Location = ({ onChangeSearchLocation }) => {
  const addrArr = [
    {
      name: "서울",
      subArea: [
        "강남구",
        "강동구",
        "강북구",
        "강서구",
        "관악구",
        "광진구",
        "구로구",
        "금천구",
        "노원구",
        "도봉구",
        "동대문구",
        "동작구",
        "마포구",
        "서대문구",
        "서초구",
        "성동구",
        "성북구",
        "송파구",
        "양천구",
        "영등포구",
        "용산구",
        "은평구",
        "종로구",
        "중구",
        "중랑구",
      ],
    },
    {
      name: "경기",
      subArea: [
        "고양시",
        "과천시",
        "광명시",
        "광주시",
        "구리시",
        "군포시",
        "김포시",
        "남양주시",
        "동두천시",
        "부천시",
        "성남시",
        "수원시",
        "시흥시",
        "안산시",
        "안성시",
        "안양시",
        "양주시",
        "오산시",
        "용인시",
        "의왕시",
        "의정부시",
        "이천시",
        "파주시",
        "평택시",
        "포천시",
        "하남시",
        "화성시",
        "가평군",
        "양평군",
        "여주군",
        "연천군",
      ],
    },
    {
      name: "인천",
      subArea: [
        "인천계양구",
        "인천미추홀구",
        "인천남동구",
        "인천동구",
        "인천부평구",
        "인천서구",
        "인천연수구",
        "인천중구",
        "인천강화군",
        "인천옹진군",
      ],
    },
    {
      name: "대전",
      subArea: ["대전대덕구", "대전동구", "대전서구", "대전유성구", "대전중구"],
    },
    {
      name: "대구",
      subArea: [
        "대구남구",
        "대구달서구",
        "대구동구",
        "대구북구",
        "대구서구",
        "대구수성구",
        "대구중구",
        "대구달성군",
        "대구군위군",
      ],
    },
    {
      name: "부산",
      subArea: [
        "강서구",
        "금정구",
        "남구",
        "동구",
        "동래구",
        "부산진구",
        "북구",
        "사상구",
        "사하구",
        "서구",
        "수영구",
        "연제구",
        "영도구",
        "중구",
        "해운대구",
        "기장군",
      ],
    },
    {
      name: "울산",
      subArea: ["울산남구", "울산동구", "울산북구", "울산중구", "울산울주군"],
    },
    {
      name: "광주",
      subArea: ["광주광산구", "광주남구", "광주동구", "광주북구", "광주서구"],
    },
    {
      name: "강원",
      subArea: [
        "강릉시",
        "동해시",
        "삼척시",
        "속초시",
        "원주시",
        "춘천시",
        "태백시",
        "고성군",
        "양구군",
        "양양군",
        "영월군",
        "인제군",
        "정선군",
        "철원군",
        "평창군",
        "홍천군",
        "화천군",
        "횡성군",
      ],
    },
    {
      name: "충북",
      subArea: [
        "제천시",
        "청주시",
        "충주시",
        "괴산군",
        "단양군",
        "보은군",
        "영동군",
        "옥천군",
        "음성군",
        "증평군",
        "진천군",
        "청원군",
      ],
    },

    {
      name: "충남",
      subArea: [
        "계룡시",
        "공주시",
        "논산시",
        "보령시",
        "서산시",
        "아산시",
        "천안시",
        "금산군",
        "당진군",
        "부여군",
        "서천군",
        "연기군",
        "예산군",
        "청양군",
        "태안군",
        "홍성군",
      ],
    },

    {
      name: "경북",
      subArea: [
        "경산시",
        "경주시",
        "구미시",
        "김천시",
        "문경시",
        "상주시",
        "안동시",
        "영주시",
        "영천시",
        "포항시",
        "고령군",
        "군위군",
        "봉화군",
        "성주군",
        "영덕군",
        "영양군",
        "예천군",
        "울릉군",
        "울진군",
        "의성군",
        "청도군",
        "청송군",
        "칠곡군",
      ],
    },
    {
      name: "경남",
      subArea: [
        "거제시",
        "김해시",
        "마산시",
        "밀양시",
        "사천시",
        "양산시",
        "진주시",
        "진해시",
        "창원시",
        "통영시",
        "거창군",
        "고성군",
        "남해군",
        "산청군",
        "의령군",
        "창녕군",
        "하동군",
        "함안군",
        "함양군",
        "합천군",
      ],
    },
    {
      name: "전북",
      subArea: [
        "군산시",
        "김제시",
        "남원시",
        "익산시",
        "전주시",
        "정읍시",
        "고창군",
        "무주군",
        "부안군",
        "순창군",
        "완주군",
        "임실군",
        "장수군",
        "진안군",
      ],
    },
    {
      name: "전남",
      subArea: [
        "광양시",
        "나주시",
        "목포시",
        "순천시",
        "여수시",
        "강진군",
        "고흥군",
        "곡성군",
        "구례군",
        "담양군",
        "무안군",
        "보성군",
        "신안군",
        "영광군",
        "영암군",
        "완도군",
        "장성군",
        "장흥군",
        "진도군",
        "함평군",
        "해남군",
        "화순군",
      ],
    },
    {
      name: "제주",
      subArea: ["서귀포시", "제주시"],
    },
  ];

  const [selectLocation, setSelectLocation] = useState({
    sidoCdNm: "",
    sgguCdNm: "",
  });

  useEffect(() => {
    onChangeSearchLocation(selectLocation);
  }, [selectLocation]);

  const onChangeSido = (e) => {
    console.log(e.target.value);
    setSelectLocation({ sidoCdNm: e.target.value, sgguCdNm: "" });
  };

  const onChangeSggu = (e) => {
    console.log(e.target.value);
    setSelectLocation({ ...selectLocation, sgguCdNm: e.target.value });
  };

  return (
    <div>
      <div class="centered-form">
        <select id="sidoSelect" name="sidoCdNm" onChange={onChangeSido}>
          <option value="">도/시를 선택하세요</option>
          {addrArr.map((item) => (
            <option value={item.name}>{item.name}</option>
          ))}
        </select>
        <select id="sgguSelect" name="sgguCdNm" onChange={onChangeSggu}>
          <option value="" selected>
            구/군을 선택하세요
          </option>
          {addrArr
            .filter((item) => item.name === selectLocation.sidoCdNm)
            .map((item) =>
              item.subArea.map((subArea) => (
                <option value={subArea}>{subArea}</option>
              ))
            )}
        </select>
      </div>
    </div>
  );
};
export default Location;
