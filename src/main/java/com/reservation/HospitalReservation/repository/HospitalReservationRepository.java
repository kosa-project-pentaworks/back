package com.reservation.HospitalReservation.repository;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.repository.custom.HospitalReservationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalReservationRepository extends JpaRepository<HospitalReservationEntity, Long>, HospitalReservationRepositoryCustom {
}
