package com.reservation.hospitalReviews.controller;

import com.reservation.global.response.CustomApiResponse;
import com.reservation.hospitalReviews.controller.requerst.HospitalReviewInputRequest;
import com.reservation.hospitalReviews.controller.requerst.HospitalReviewUpdateInputRequest;
import com.reservation.hospitalReviews.service.HospitalReviewService;
import com.reservation.hospitalReviews.service.response.FindHospitalReviewPageResponse;
import com.reservation.hospitalReviews.service.response.FindOneHospitalReviewResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class HospitalReviewController {
    private final HospitalReviewService hospitalReviewService;

    public HospitalReviewController(HospitalReviewService hospitalReviewService) {
        this.hospitalReviewService = hospitalReviewService;
    }

    @GetMapping("/api/v1/hospitalreview")
    public CustomApiResponse<FindOneHospitalReviewResponse> findOneHospitalReview(
        @RequestParam(value = "hospitalReviewId") Long hospitalReviewId
    ){
        FindOneHospitalReviewResponse findOneHospitalReview = hospitalReviewService.findOneHospitalReviewByHospitalReviewId(hospitalReviewId);
        return CustomApiResponse.ok(findOneHospitalReview);
    }

    @PostMapping("/api/v1/hospitalreview/save")
    public CustomApiResponse<Boolean> saveReview(
        @RequestBody HospitalReviewInputRequest hospitalReviewInput
        ){
        hospitalReviewService.saveHospitalReview(hospitalReviewInput);
        return  CustomApiResponse.ok(true);
    }

    @PostMapping("/api/v1/hospitalreview/update")
    public CustomApiResponse<Boolean> updateReview(
        @RequestBody HospitalReviewUpdateInputRequest hospitalReviewUpdateInput
        ){
        hospitalReviewService.updateHospitalReview(hospitalReviewUpdateInput);
        return CustomApiResponse.ok(true);
    }

    @GetMapping("/api/v1/hospitalreview/find")
    public CustomApiResponse<FindHospitalReviewPageResponse> findReview(
        @RequestParam(value = "hospId") Long hospId,
        @RequestParam(value = "page",defaultValue = "1") int page,
        @RequestParam(value ="size" ,defaultValue = "10") int size
    ){
        FindHospitalReviewPageResponse findReview = hospitalReviewService.findHospitalReviewByHopitalId(hospId,page,size);
        return CustomApiResponse.ok(findReview);
    }
}
