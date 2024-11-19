package com.reservation.payment.controller.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class CreatePaymentRequest {
    private String impUid;
    private int amount;
    private Long hospId;
    private LocalDate reservationAt;
    private LocalTime reservationTime;
    private String redisKey;

    public CreatePaymentRequest() {
    }

    public CreatePaymentRequest(String impUid, int amount, Long hospId, LocalDate reservationAt, LocalTime reservationTime, String redisKey) {
        this.impUid = impUid;
        this.amount = amount;
        this.hospId = hospId;
        this.reservationAt = reservationAt;
        this.reservationTime = reservationTime;
        this.redisKey = redisKey;
    }

}
