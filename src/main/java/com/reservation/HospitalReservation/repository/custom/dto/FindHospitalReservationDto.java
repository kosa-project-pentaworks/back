package com.reservation.HospitalReservation.repository.custom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reservation.HospitalReservation.domain.ReservationStatus;
import com.reservation.hospitalReviews.domain.HospitalReviewEntity;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.payment.domain.PaymentEntity;
import com.reservation.user.domain.UserEntity;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class FindHospitalReservationDto {
    private Long hospReservationId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate reservationAt;
    private String reservationTime;
    private Long userId;
    private Long hospId;
    private ReservationStatus reservationStatus;
    private String yadmNm;
    private String addr;
    private String telno;
    private Long hospReviewId;


    public FindHospitalReservationDto(LocalDate reservationAt, LocalTime reservationTime) {
        this.reservationAt = reservationAt;
        this.reservationTime = dateTimeFormatter(reservationTime);
    }

    public FindHospitalReservationDto(Long hospId,LocalDate reservationAt, LocalTime reservationTime) {
        this.hospId = hospId;
        this.reservationAt = reservationAt;
        this.reservationTime = dateTimeFormatter(reservationTime);
    }


    public FindHospitalReservationDto(Long hospReservationId, LocalDate reservationAt, LocalTime reservationTime, ReservationStatus reservationStatus, Long userId, Long hospId, String yadmNm, String addr, String telno) {
        this.hospReservationId = hospReservationId;
        this.reservationAt = reservationAt;
        this.reservationTime = dateTimeFormatter(reservationTime);
        this.reservationStatus = reservationStatus;
        this.userId = userId;
        this.hospId = hospId;
        this.yadmNm = yadmNm;
        this.addr = addr;
        this.telno = telno;

    }

    public FindHospitalReservationDto(Long hospReservationId, LocalDate reservationAt, LocalTime reservationTime, ReservationStatus reservationStatus, Long userId, Long hospId, String yadmNm, String addr, String telno, Long hospReviewId) {
        this.hospReservationId = hospReservationId;
        this.reservationAt = reservationAt;
        this.reservationTime = dateTimeFormatter(reservationTime);
        this.reservationStatus = reservationStatus;
        this.userId = userId;
        this.hospId = hospId;
        this.yadmNm = yadmNm;
        this.addr = addr;
        this.telno = telno;
        this.hospReviewId = hospReviewId;
    }

    public static String dateTimeFormatter(LocalTime reservationTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return reservationTime.format(formatter);  // "HH:mm" 형식으로 변환
    }

}
