package com.reservation.payment.controller.request;

import lombok.Getter;

@Getter
public class RefundPaymentRequerst {
    private final String imp_uid;

    public RefundPaymentRequerst(String imp_uid) {
        this.imp_uid = imp_uid;
    }
}
