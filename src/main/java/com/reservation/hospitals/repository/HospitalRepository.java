package com.reservation.hospitals.repository;

import com.reservation.hospitals.domain.HospStatus;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.hospitals.repository.custom.HospitalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalRepository extends JpaRepository<HospitalEntity, Long>, HospitalRepositoryCustom {
    List<HospitalEntity> findByHospStatus(HospStatus hospStatus);
}
