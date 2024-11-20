package com.reservation.hospitalReviews.repository;

import com.reservation.hospitalReviews.domain.HospitalReviewEntity;
import com.reservation.hospitalReviews.repository.custom.HospitalReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalReviewRepository extends JpaRepository<HospitalReviewEntity, Long>, HospitalReviewRepositoryCustom {
}
