package com.reservation.hospitals.repository;

import com.reservation.hospitals.domain.HospStatus;
import com.reservation.hospitals.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    List<Hospital> findByHospStatus(HospStatus hospStatus);
}
