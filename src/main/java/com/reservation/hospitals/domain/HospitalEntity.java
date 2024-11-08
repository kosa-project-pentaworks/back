package com.reservation.hospitals.domain;

import com.reservation.hospitalReviews.domain.HospitalReviewEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name="hospital",
    indexes = {
        @Index(name="idx_hopital_sido_cd_nm",columnList = "sidoCdNm")
    }
)
public class HospitalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hosp_id")
    private Long hospId;
    @Column(name = "addr", length = 150, nullable = false)
    private String addr;
    @Column(name = "cl_cd", length = 4)
    private int clCd;
    @Column(name = "cl_cd_nm", length = 16, nullable = false)
    private String clCdNm;
    @Column(name = "emdong_nm", length = 16)
    private String emdongNm;
    @Column(name = "estb_dd", length = 10)
    private String estbDd;
    @Column(name = "hosp_url", length = 100)
    private String hospUrl;
    @Column(name = "post_no", length = 5, nullable = false)
    private int postNo;
    @Column(name = "sggu_cd", length = 7, nullable = false)
    private int sgguCd;
    @Column(name = "sggu_cd_nm", length = 20, nullable = false)
    private String sgguCdNm;
    @Column(name = "sido_cd", length = 10, nullable = false)
    private int sidoCd;
    @Column(name = "sido_cd_nm", length = 10, nullable = false)
    private String sidoCdNm;
    @Column(name = "telno", length = 15)
    private String telno;
    @Column(name = "yadm_nm",length = 60, nullable = false)
    private String yadmNm;
    @Column(name = "ykiho", nullable = false)
    private String ykiho;
    @Enumerated(EnumType.STRING)
    @Column(name = "hosp_status", length = 5, nullable = false)
    private HospStatus hospStatus;
    @OneToOne(mappedBy = "hospitalEntity")
    private HospitalAdmin hospitalAdmin;
    @OneToMany(mappedBy = "hospitalEntity")
    private List<HospitalReviewEntity> hospitalReviewEntity;


    public HospitalEntity(HospitalEntity hospital,HospStatus hospStatus) {
        this.hospId = hospital.getHospId();
        this.addr = hospital.getAddr();
        this.clCd = hospital.getClCd();
        this.clCdNm =hospital.getClCdNm();
        this.emdongNm = hospital.getEmdongNm();
        this.estbDd = hospital.getEstbDd();
        this.hospUrl = hospital.getHospUrl();
        this.postNo = hospital.getPostNo();
        this.sgguCd = hospital.getSgguCd();
        this.sgguCdNm = hospital.getSgguCdNm();
        this.sidoCd = hospital.getSidoCd();
        this.sidoCdNm = hospital.getSidoCdNm();
        this.telno = hospital.getTelno();
        this.yadmNm = hospital.getYadmNm();
        this.ykiho = hospital.getYkiho();
        this.hospStatus = hospStatus;
    }

    @Builder
    private HospitalEntity(String addr, int clCd, String clCdNm, String emdongNm, String estbDd, String hospUrl, int postNo, int sgguCd, String sgguCdNm, int sidoCd, String sidoCdNm, String telno, String yadmNm, String ykiho) {
        this.addr = addr;
        this.clCd = clCd;
        this.clCdNm = clCdNm;
        this.emdongNm = emdongNm;
        this.estbDd = estbDd;
        this.hospUrl = hospUrl;
        this.postNo = postNo;
        this.sgguCd = sgguCd;
        this.sgguCdNm = sgguCdNm;
        this.sidoCd = sidoCd;
        this.sidoCdNm = sidoCdNm;
        this.telno = telno;
        this.yadmNm = yadmNm;
        this.ykiho = ykiho;
        this.hospStatus = HospStatus.OPEN;
    }

}
