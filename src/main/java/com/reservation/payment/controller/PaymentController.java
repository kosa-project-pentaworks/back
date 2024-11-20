package com.reservation.payment.controller;

import com.reservation.global.redis.client.ApplicationLockClient;
import com.reservation.global.response.CustomApiResponse;
import com.reservation.payment.controller.request.CreatePaymentRequest;
import com.reservation.payment.controller.request.ReservationRefundRequest;
import com.reservation.payment.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private final PaymentService paymentService;
    private final ApplicationLockClient applicationLockClient;

    public PaymentController(PaymentService paymentService, ApplicationLockClient applicationLockClient) {
        this.paymentService = paymentService;
        this.applicationLockClient = applicationLockClient;
    }

    @PostMapping("/api/v1/payment")
    public CustomApiResponse<Boolean> createPayment(
        @RequestBody CreatePaymentRequest createPaymentRequest
        ){
        System.out.println("ㅈㅓㅇ보?? ==> " + createPaymentRequest.getProviderId());
        Boolean  isvalidPayment = paymentService.createPayment(createPaymentRequest);
        if(isvalidPayment){
        }
        return CustomApiResponse.ok(true);
    }

    @PostMapping("/api/v1/payment/redisremove")
    public void redisRemove(
        @RequestBody String redisKey
    ){
        applicationLockClient.remove(redisKey);
    }

    @PostMapping("/api/v1/payment/refund")
    public CustomApiResponse<Boolean> refund(
        @RequestBody ReservationRefundRequest reservationRefund
        ){
        System.out.println("환불 들어오나?");
        System.out.println("?? ==>> " + reservationRefund.getReservationId());
        Boolean result = paymentService.refund(reservationRefund.getReservationId());
        return CustomApiResponse.ok(result);

    }
}
