package com.reservation.hospitalReviews.repository.custom;

import com.reservation.hospitalReviews.repository.custom.dto.FindHospitalReviewDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface HospitalReviewRepositoryCustom {
    PageImpl<FindHospitalReviewDto> findReviewByHospitalId(Long hospId, Pageable pageable);
}
