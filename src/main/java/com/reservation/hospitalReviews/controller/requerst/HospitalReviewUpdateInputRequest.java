package com.reservation.hospitalReviews.controller.requerst;

import lombok.Getter;

@Getter
public class HospitalReviewUpdateInputRequest {
    private Long hospReviewId;
    private String content;
    private int rating;
    private Long userId;
    private Long hospId;
    private Long hospitalReservationId;

    public HospitalReviewUpdateInputRequest() {
    }
}
