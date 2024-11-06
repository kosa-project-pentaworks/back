package com.reservation.hospitals.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name="hospital",
    indexes = {
        @Index(name="idx_hopital_sido_cd_nm",columnList = "sidoCdNm")
    }
)
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 150, nullable = false)
    private String addr;
    @Column(length = 4)
    private int clCd;
    @Column(length = 16, nullable = false)
    private String clCdNm;
    @Column(length = 16)
    private String emdongNm;
    @Column(length = 10)
    private String estbDd;
    @Column(length = 100)
    private String hospUrl;
    @Column(length = 5, nullable = false)
    private int postNo;
    @Column(length = 7, nullable = false)
    private int sgguCd;
    @Column(length = 20, nullable = false)
    private String sgguCdNm;
    @Column(length = 10, nullable = false)
    private int sidoCd;
    @Column(length = 10, nullable = false)
    private String sidoCdNm;
    @Column(length = 15)
    private String telno;
    @Column(length = 60, nullable = false)
    private String yadmNm;
    @Column(nullable = false)
    private String ykiho;
    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
//    @ColumnDefault("HospStatus.OPEN")
    private HospStatus hospStatus;


    public Hospital(Hospital hospital,HospStatus hospStatus) {
        this.id = hospital.getId();
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
    private Hospital(String addr, int clCd, String clCdNm, String emdongNm, String estbDd, String hospUrl, int postNo, int sgguCd, String sgguCdNm, int sidoCd, String sidoCdNm, String telno, String yadmNm, String ykiho) {
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
