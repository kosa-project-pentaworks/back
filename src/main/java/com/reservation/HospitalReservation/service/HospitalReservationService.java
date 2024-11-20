package com.reservation.HospitalReservation.service;

import com.reservation.HospitalReservation.controller.request.ReservationSaveRequest;
import com.reservation.HospitalReservation.controller.request.ReservationUpdateRequest;
import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.repository.HospitalReservationRepository;
import com.reservation.HospitalReservation.repository.custom.dto.FindHospitalReservationDto;
import com.reservation.HospitalReservation.repository.custom.dto.FindOneHospitalReservationPaymentDto;
import com.reservation.HospitalReservation.service.HospitalReservationResponse.HospitalReservationPageResponse;
import com.reservation.HospitalReservation.service.HospitalReservationResponse.ReservationIsvalidAndLockResponse;
import com.reservation.global.redis.client.ApplicationLockClient;
import com.reservation.global.redis.lock.RedisLock;
import com.reservation.payment.domain.PaymentEntity;
import com.reservation.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class HospitalReservationService {
    private final HospitalReservationRepository hospitalReservationRepository;
    private final RedisLock redisLock;
    private final ApplicationLockClient applicationLockClient;

    public HospitalReservationService(HospitalReservationRepository hospitalReservationRepository, RedisLock redisLock, ApplicationLockClient applicationLockClient) {
        this.hospitalReservationRepository = hospitalReservationRepository;
        this.redisLock = redisLock;
        this.applicationLockClient = applicationLockClient;
    }

    @Transactional(readOnly = true)
    public List<FindHospitalReservationDto> findAllReservationByHospIdAndReservationDate(Long hospId, LocalDate reservationAt){
        return hospitalReservationRepository.findAllByHospitalIdAndReservationDate(hospId, reservationAt);
    }

    @Transactional
    public Boolean createReservation(HospitalReservationEntity reservation){
        HospitalReservationEntity createdReservation = hospitalReservationRepository.save(reservation);
        return createdReservation.getHospReservationId() != null;
    }

    // 예약된 시간이 있는지 확인한다.
    @Transactional(readOnly = true)
    public ReservationIsvalidAndLockResponse isValidReservaion(ReservationSaveRequest request){
        // 레디스에 예약중인지 체크
        if(!applicationLockClient.isvalidRedisKey(request.getHospId(), request.getReservationAt(), request.getReservationTime())) {
            throw new RuntimeException();
        }
        // 키값을 락을 걸어 키를 레디스에 저장후 락을 푼다.
        String redisKey = reservationLock(request);
        // false면 예약이 가능하다.
        Boolean isvalid = hospitalReservationRepository.findByHospIdAndReservationDateAndReservationTime(request).isPresent();
        if(isvalid) applicationLockClient.remove(redisKey);
        return new ReservationIsvalidAndLockResponse(redisKey, isvalid);
    }

    // 업데이트 할것.


    // redis 키값을 반환한다.
    @Transactional
    public String reservationLock(ReservationSaveRequest request){
        return redisLock.lockReservation(request.getHospId(), request.getReservationAt(), request.getReservationTime());
    }

    @Transactional(readOnly = true)
    public FindOneHospitalReservationPaymentDto findOneHospitalReservation(Long hospitalReservationId){
        return hospitalReservationRepository.findOneHospitalReservation(hospitalReservationId);
    }
    @Transactional(readOnly = true)
    public HospitalReservationPageResponse findAllHospitalReservationByUserId(String providerId, String type, int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        PageImpl<FindHospitalReservationDto> findHospitalRservation =  hospitalReservationRepository.findAllByUserId(providerId, type,pageable);
        return HospitalReservationPageResponse.response(findHospitalRservation.getNumber(), findHospitalRservation.getTotalPages(), findHospitalRservation.hasPrevious(), findHospitalRservation.hasNext(), findHospitalRservation.getContent());
    }

    // 예약 정보 수정
    @Transactional
    public void updateHospitalReservation(ReservationUpdateRequest reservationUpdate){
        HospitalReservationEntity findOneReservation = hospitalReservationRepository.findOneByReservationId(reservationUpdate.getReservationId());
        PaymentEntity updatePayment = findOneReservation.getPayment();
        updatePayment.updateStatus(reservationUpdate.getReservationAt());
        findOneReservation.update(reservationUpdate);
    }


}
