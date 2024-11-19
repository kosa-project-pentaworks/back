package com.reservation.payment.controller.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentTokentRequest {
    private String imp_key;
    private String imp_secret;

    public PaymentTokentRequest(String imp_key, String imp_secret) {
        this.imp_key = imp_key;
        this.imp_secret = imp_secret;
    }
}
