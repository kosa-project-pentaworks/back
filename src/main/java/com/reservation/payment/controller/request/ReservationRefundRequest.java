package com.reservation.payment.controller.request;

import lombok.Getter;

@Getter
public class ReservationRefundRequest {
    private Long reservationId;

    public ReservationRefundRequest() {
    }
}
