package com.reservation.payment.domain;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.domain.ReservationStatus;
import com.reservation.global.audit.MutableBaseEntity;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.payment.controller.request.CreatePaymentRequest;
import com.reservation.user.domain.SocialUserEntity;
import com.reservation.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity extends MutableBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "import_uid", nullable = false)
    private String importUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 12, nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "amount", length = 4, nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SocialUserEntity socialUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosp_id")
    private HospitalEntity hospital;



    @Builder
    public PaymentEntity(Long paymentId, String importUid, PaymentStatus paymentStatus, int amount, SocialUserEntity socialUser, HospitalEntity hospital) {
        this.paymentId = paymentId;
        this.importUid = importUid;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.socialUser = socialUser;
        this.hospital = hospital;
    }

    public static PaymentEntity from(CreatePaymentRequest createPaymentRequest, HospitalEntity hospital, SocialUserEntity user){
        return PaymentEntity.builder()
            .importUid(createPaymentRequest.getImpUid())
            .amount(createPaymentRequest.getAmount())
            .hospital(hospital)
            .paymentStatus(checkPaymentStatus(createPaymentRequest.getReservationAt()))
            .socialUser(user)
            .build();
    }

    public PaymentEntity updateStatus(LocalDate now){
       this.paymentStatus = checkPaymentStatus(now);
       return this;
    }

    public PaymentEntity updateStatus(PaymentStatus status){
        this.paymentStatus = status;
        return this;
    }
    public static PaymentStatus checkPaymentStatus(LocalDate date){
        LocalDate now = LocalDate.now();
        if(now.equals(date)){
            return PaymentStatus.END;
        }else {
            return PaymentStatus.SUCCESS;
        }
    }

}
