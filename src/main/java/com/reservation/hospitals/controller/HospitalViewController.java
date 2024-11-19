package com.reservation.hospitals.controller;

import com.reservation.hospitals.domain.dto.HospitalSearchDto;
import com.reservation.hospitals.service.HospitalService;
import com.reservation.hospitals.service.response.HospitalsPageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HospitalViewController {
    private final HospitalService hospitalService;

    public HospitalViewController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping("/api/v1/hopitalsearch")
    public String searchHospital(
        @RequestParam(value = "sidoCdNm", defaultValue="") String sidoCdNm,
        @RequestParam(value = "sgguCdNm", defaultValue="") String sgguCdNm,
        @RequestParam(value = "keyWord", defaultValue="") String keyWord,
        @RequestParam(value = "page",defaultValue = "1") int page,
        @RequestParam(value ="size" ,defaultValue = "10") int size,
        Model model
        ){
        HospitalsPageResponse hospitalsPages = hospitalService.searchHospital(sidoCdNm,sgguCdNm,keyWord,size,page);
        model.addAttribute("hospitalPages", hospitalsPages);
        for(HospitalSearchDto aa : hospitalsPages.getHospitals()){
            System.out.println(aa.getYadmNm());
        }
        return "fragments/hospital/hospitals";
//        return "fragments/index";
    }
}
