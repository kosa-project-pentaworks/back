package com.reservation.HospitalReservation.repository.custom;

import com.reservation.HospitalReservation.controller.request.ReservationSaveRequest;
import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.repository.custom.dto.FindHospitalReservationDto;
import com.reservation.HospitalReservation.repository.custom.dto.FindOneHospitalReservationPaymentDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HospitalReservationRepositoryCustom {
    List<FindHospitalReservationDto>  findAllByHospitalIdAndReservationDate(Long hospId, LocalDate reservationDate);
    Optional<FindHospitalReservationDto> findByHospIdAndReservationDateAndReservationTime(ReservationSaveRequest request);
    FindOneHospitalReservationPaymentDto findOneHospitalReservation(Long hospitalReservationId);
    List<FindHospitalReservationDto> findHospitalReservationByUserId(Long UserId);
    List<HospitalReservationEntity> findAllByReservationAt(LocalDate reservationAt);
    PageImpl<FindHospitalReservationDto> findAllByUserId(Long userId, String select, Pageable pageable);
    HospitalReservationEntity findOneByReservationId(Long reservationId);

}
