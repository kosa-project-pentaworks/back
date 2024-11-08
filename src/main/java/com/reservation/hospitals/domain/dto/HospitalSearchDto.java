package com.reservation.hospitals.domain.dto;

import lombok.Getter;

@Getter
public class HospitalSearchDto {
    private Long hospId;
    private String addr;
    private String yadmNm;
    private String telno;
    private Long hospAdminId;
    private Long reviewCount;
    private Double ratingAvg;

    public HospitalSearchDto(Long hospId, String addr, String yadmNm, String telno, Long hospAdminId, Long reviewCount, Double ratingAvg) {
        this.hospId = hospId;
        this.addr = addr;
        this.yadmNm = yadmNm;
        this.telno = telno;
        this.hospAdminId = hospAdminId;
        this.reviewCount = reviewCount;
        this.ratingAvg = checkRating(ratingAvg);
    }

    public Double checkRating(Double ratingAvg){
        if(ratingAvg == null) return 0.0;
        else return ratingAvg;
    }
}
