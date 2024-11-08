package com.reservation.hospitals.service;

import com.reservation.hospitals.domain.dto.HospitalSearchDto;
import com.reservation.hospitals.repository.HospitalRepository;
import com.reservation.hospitals.service.response.HospitalsPageResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public HospitalsPageResponse searchHospital(String sidoCdNm, String sgguCdNm ,String keyWord, int size, int page){
        Pageable pageable = PageRequest.of(page - 1, size);
        PageImpl<HospitalSearchDto> aa = hospitalRepository.findHospitalAll(sidoCdNm,sgguCdNm,keyWord,pageable);

        return HospitalsPageResponse.builder()
            .number(aa.getNumber())
            .previous(aa.hasPrevious())
            .totalPage(aa.getTotalPages())
            .next(aa.hasNext())
            .hospitals(aa.getContent())
            .build();
    }
}
