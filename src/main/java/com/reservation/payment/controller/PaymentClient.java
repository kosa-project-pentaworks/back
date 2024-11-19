package com.reservation.payment.controller;

//import com.reservation.global.config.FeignClientConfig;
import com.reservation.payment.controller.request.RefundPaymentRequerst;
import com.reservation.payment.controller.response.CheckPaymentResponse;
import com.reservation.payment.controller.request.PaymentTokentRequest;
import com.reservation.payment.controller.response.PaymentRefundResponse;
import com.reservation.payment.controller.response.PaymentTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "payment", url = "https://api.iamport.kr")
public interface PaymentClient {
    @PostMapping("/users/getToken")
    PaymentTokenResponse getToken(PaymentTokentRequest paymentTokentRequest);

    @GetMapping("/payments/{imp_uid}")
    CheckPaymentResponse checkPayment(
        @PathVariable(value = "imp_uid") String imp_uid,
        @RequestHeader("Authorization") String headerAccessToken);

    @PostMapping("/payments/cancel")
    PaymentRefundResponse refund(
        @RequestBody RefundPaymentRequerst refundPaymentRequerst,
//        @PathVariable(value = "imp_uid") String impUid,
        @RequestHeader("Authorization") String headerAccessToken
    );

}

