package com.reservation.hospitalReviews.repository.custom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class FindHospitalReviewDto {
    private final String username;
    private final String hospReviewContent;
    private final int hospReviewRating;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate reservationAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private final LocalTime reservationTime;

    public FindHospitalReviewDto(String username, String hospReviewContent, int hospReviewRating, LocalDate reservationAt, LocalTime reservationTime) {
        this.username = username;
        this.hospReviewContent = hospReviewContent;
        this.hospReviewRating = hospReviewRating;
        this.reservationAt = reservationAt;
        this.reservationTime = reservationTime;
    }
}
