package com.reservation.hospitalReviews.service;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.domain.ReservationStatus;
import com.reservation.HospitalReservation.repository.HospitalReservationRepository;
import com.reservation.hospitalReviews.controller.requerst.HospitalReviewInputRequest;
import com.reservation.hospitalReviews.controller.requerst.HospitalReviewUpdateInputRequest;
import com.reservation.hospitalReviews.domain.HospitalReviewEntity;
import com.reservation.hospitalReviews.repository.HospitalReviewRepository;
import com.reservation.hospitalReviews.service.response.FindOneHospitalReviewResponse;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.hospitals.repository.HospitalRepository;
import com.reservation.user.domain.SocialUserEntity;
import com.reservation.user.repository.social.SocialUserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HospitalReviewService {
    private final HospitalReviewRepository hospitalReviewRepository;
    private final SocialUserJpaRepository userJpaRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalReservationRepository hospitalReservationRepository;

    public HospitalReviewService(HospitalReviewRepository hospitalReviewRepository, SocialUserJpaRepository userJpaRepository, HospitalRepository hospitalRepository, HospitalReservationRepository hospitalReservationRepository) {
        this.hospitalReviewRepository = hospitalReviewRepository;
        this.userJpaRepository = userJpaRepository;
        this.hospitalRepository = hospitalRepository;
        this.hospitalReservationRepository = hospitalReservationRepository;
    }

    @Transactional
    public void saveHospitalReview(HospitalReviewInputRequest hospitalReviewInput){
        Optional<SocialUserEntity> user = userJpaRepository.findByProviderId("3782292691");
        Optional<HospitalEntity> hospital = hospitalRepository.findById(hospitalReviewInput.getHospId());
        Optional<HospitalReservationEntity> findReservation = hospitalReservationRepository.findById(hospitalReviewInput.getHospitalReservationId());
        HospitalReservationEntity updateReservation = findReservation.get();
        updateReservation.updateStatus(ReservationStatus.END);
        HospitalReviewEntity hospitalReview = HospitalReviewEntity.save(hospitalReviewInput, user.get(), hospital.get(), updateReservation);
        hospitalReviewRepository.save(hospitalReview);
    }

    public FindOneHospitalReviewResponse findOneHospitalReviewByHospitalReviewId(Long hospitalReviewId){
        Optional<HospitalReviewEntity> findHospitalReview = hospitalReviewRepository.findById(hospitalReviewId);
        return FindOneHospitalReviewResponse.findOne(findHospitalReview.get());
    }
    @Transactional
    public void updateHospitalReview(HospitalReviewUpdateInputRequest hospitalReviewUpdateInput){
        Optional<HospitalReviewEntity> findHospitalReview = hospitalReviewRepository.findById(hospitalReviewUpdateInput.getHospReviewId());
        HospitalReviewEntity updateHospitalReview = findHospitalReview.get();
        updateHospitalReview.update(hospitalReviewUpdateInput);


    }
}
