package com.reservation.hospitalReviews.service.response;

import com.reservation.hospitalReviews.domain.HospitalReviewEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FindOneHospitalReviewResponse {
    private final Long hospReviewId;
    private final String hospReviewContent;
    private final int hospReviewRating;

    @Builder
    public FindOneHospitalReviewResponse(Long hospReviewId, String hospReviewContent, int hospReviewRating) {
        this.hospReviewId = hospReviewId;
        this.hospReviewContent = hospReviewContent;
        this.hospReviewRating = hospReviewRating;
    }

    public static FindOneHospitalReviewResponse findOne(HospitalReviewEntity hospitalReview){
        return FindOneHospitalReviewResponse.builder()
            .hospReviewId(hospitalReview.getHospReviewId())
            .hospReviewContent(hospitalReview.getHospReviewContent())
            .hospReviewRating(hospitalReview.getHospReviewRating())
            .build();
    }
}
