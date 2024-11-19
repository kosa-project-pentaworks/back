package com.reservation.HospitalReservation.domain;

import com.reservation.HospitalReservation.controller.request.ReservationUpdateRequest;
import com.reservation.global.audit.MutableBaseEntity;
import com.reservation.hospitalReviews.domain.HospitalReviewEntity;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.payment.controller.request.CreatePaymentRequest;
import com.reservation.payment.domain.PaymentEntity;
import com.reservation.user.domain.SocialUserEntity;
import com.reservation.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@ToString
@Table(name = "hospital_reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalReservationEntity extends MutableBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hosp_reservation_id")
    private Long hospReservationId;
    @Column(name = "reservation_at", nullable = false)
    private LocalDate reservationAt;
    @Column(name = "reservation_time", nullable = false)
    private LocalTime reservationTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status",nullable = false)
    private ReservationStatus reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SocialUserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosp_id")
    private HospitalEntity hospital;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    @OneToOne(mappedBy = "hospitalReservation")
    private HospitalReviewEntity hospitalReview;

    @Builder
    public HospitalReservationEntity(Long hospReservationId, LocalDate reservationAt, LocalTime reservationTime, ReservationStatus reservationStatus, SocialUserEntity user, HospitalEntity hospital, PaymentEntity payment) {
        this.hospReservationId = hospReservationId;
        this.reservationAt = reservationAt;
        this.reservationTime = reservationTime;
        this.reservationStatus = reservationStatus;
        this.user = user;
        this.hospital = hospital;
        this.payment = payment;
    }

    public static HospitalReservationEntity from(CreatePaymentRequest createPaymentRequest, SocialUserEntity user, HospitalEntity hospital, PaymentEntity payment){
        return HospitalReservationEntity.builder()
            .reservationAt(createPaymentRequest.getReservationAt())
            .reservationTime(createPaymentRequest.getReservationTime())
            .user(user)
            .reservationStatus(checkReservationStatus(createPaymentRequest.getReservationAt()))
            .hospital(hospital)
            .payment(payment)
            .build();
    }
    public HospitalReservationEntity updateStatus(LocalDate now, PaymentEntity updatePayment){
        this.reservationStatus = checkReservationStatus(now);
        this.payment = updatePayment;
        return this;
    }
    public HospitalReservationEntity updateStatus(ReservationStatus reservationStatus){
        this.reservationStatus = reservationStatus;
        return this;
    }
    public HospitalReservationEntity update(ReservationUpdateRequest reservationUpdate){
        this.reservationStatus = checkReservationStatus(reservationUpdate.getReservationAt());
        this.reservationAt = reservationUpdate.getReservationAt();
        this.reservationTime = reservationUpdate.getReservationTime();
        return this;
    }


    public static ReservationStatus checkReservationStatus(LocalDate date){
        LocalDate now = LocalDate.now();
        if(now.equals(date)){
//            return ReservationStatus.PENDING;
            return ReservationStatus.SUCCESS;
        }else {
            return ReservationStatus.PENDING;
        }
    }
}
