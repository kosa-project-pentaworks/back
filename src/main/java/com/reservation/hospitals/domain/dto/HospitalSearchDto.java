package com.reservation.hospitals.domain.dto;

import lombok.Getter;

@Getter
public class HospitalSearchDto {
    private Long hospId;
    private String addr;
    private String yadmNm;
    private String telno;
    private Long reviewCount;
    private Double ratingAvg;

    public HospitalSearchDto(Long hospId, String addr, String yadmNm, String telno, Long reviewCount, Double ratingAvg) {
        this.hospId = hospId;
        this.addr = addr;
        this.yadmNm = yadmNm;
        this.telno = telno;
        this.reviewCount = reviewCount;
        this.ratingAvg = checkRating(ratingAvg);
    }

    public HospitalSearchDto(Long hospId, String addr, String yadmNm, String telno) {
        this.hospId = hospId;
        this.addr = addr;
        this.yadmNm = yadmNm;
        this.telno = telno;
    }

    public Double checkRating(Double ratingAvg){
        if(ratingAvg == null) return 0.0;
        else return ratingAvg;
    }
}
