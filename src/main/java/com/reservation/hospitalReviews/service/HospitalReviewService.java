package com.reservation.hospitalReviews.service;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.domain.ReservationStatus;
import com.reservation.HospitalReservation.repository.HospitalReservationRepository;
import com.reservation.global.exception.HospitalException;
import com.reservation.global.exception.HospitalReservationException;
import com.reservation.global.exception.HospitalReviewException;
import com.reservation.global.exception.UserException;
import com.reservation.hospitalReviews.controller.requerst.HospitalReviewInputRequest;
import com.reservation.hospitalReviews.controller.requerst.HospitalReviewUpdateInputRequest;
import com.reservation.hospitalReviews.domain.HospitalReviewEntity;
import com.reservation.hospitalReviews.repository.HospitalReviewRepository;
import com.reservation.hospitalReviews.repository.custom.dto.FindHospitalReviewDto;
import com.reservation.hospitalReviews.service.response.FindHospitalReviewPageResponse;
import com.reservation.hospitalReviews.service.response.FindOneHospitalReviewResponse;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.hospitals.repository.HospitalRepository;
import com.reservation.user.domain.SocialUserEntity;
import com.reservation.user.repository.social.SocialUserJpaRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        SocialUserEntity user = userJpaRepository.findByProviderId(hospitalReviewInput.getProviderId())
            .orElseThrow(UserException.UserDoesNotExistException::new);
        HospitalEntity hospital = hospitalRepository.findById(hospitalReviewInput.getHospId())
            .orElseThrow(HospitalException.HospitalDoesNotExitException::new);
        HospitalReservationEntity findReservation = hospitalReservationRepository
            .findById(hospitalReviewInput.getHospitalReservationId())
            .orElseThrow(HospitalReservationException.HospitalReservationDoesNotExit::new);

        findReservation.updateStatus(ReservationStatus.END);
        HospitalReviewEntity hospitalReview = HospitalReviewEntity.save(hospitalReviewInput, user, hospital, findReservation);
        hospitalReviewRepository.save(hospitalReview);
    }

    public FindOneHospitalReviewResponse findOneHospitalReviewByHospitalReviewId(Long hospitalReviewId){
        HospitalReviewEntity findHospitalReview = hospitalReviewRepository.findById(hospitalReviewId)
            .orElseThrow(HospitalReviewException.HospitalReviewDoesNotExit::new);
        return FindOneHospitalReviewResponse.findOne(findHospitalReview);
    }
    @Transactional
    public void updateHospitalReview(HospitalReviewUpdateInputRequest hospitalReviewUpdateInput){
        HospitalReviewEntity findHospitalReview = hospitalReviewRepository.findById(hospitalReviewUpdateInput.getHospReviewId())
            .orElseThrow(HospitalReviewException.HospitalReviewDoesNotExit::new);
        findHospitalReview.update(hospitalReviewUpdateInput);
    }

    @Transactional(readOnly = true)
    public FindHospitalReviewPageResponse findHospitalReviewByHopitalId(Long hospId, int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        PageImpl<FindHospitalReviewDto> reveiewPages = hospitalReviewRepository.findReviewByHospitalId(hospId, pageable);
        return new FindHospitalReviewPageResponse(reveiewPages);
    }

}
