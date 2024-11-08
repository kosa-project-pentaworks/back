package com.reservation.hospitals.repository.custom;

import com.reservation.hospitals.domain.dto.HospitalSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface HospitalRepositoryCustom {
    PageImpl<HospitalSearchDto> findHospitalAll(String sidoCdNm, String sgguCdNm, String keyword, Pageable pageable);
}
