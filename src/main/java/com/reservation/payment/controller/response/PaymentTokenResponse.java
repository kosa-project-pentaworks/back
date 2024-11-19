package com.reservation.payment.controller.response;

import lombok.Getter;

@Getter
public class PaymentTokenResponse {
    private ResponseData response;

    @Getter
    public static class ResponseData {
        private String access_token;
    }
}