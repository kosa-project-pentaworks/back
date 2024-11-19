package com.reservation.hospitalReviews.controller.requerst;


import lombok.Getter;

@Getter
public class HospitalReviewInputRequest {
    private String content;
    private int rating;
    private Long hospitalReservationId;
    private Long hospId;
    private Long userId;

    public HospitalReviewInputRequest() {
    }
}
