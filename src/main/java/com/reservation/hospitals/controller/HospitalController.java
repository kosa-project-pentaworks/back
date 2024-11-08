package com.reservation.hospitals.controller;

import com.reservation.global.response.CustomApiResponse;
import com.reservation.hospitals.domain.dto.HospitalSearchDto;
import com.reservation.hospitals.service.HospitalService;
import com.reservation.hospitals.service.response.HospitalsPageResponse;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HospitalController {
    private final HospitalDataClient hospitalDataClient;
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final HospitalService hospitalService;

    public HospitalController(HospitalDataClient hospitalDataClient, JobLauncher jobLauncher, JobRegistry jobRegistry, HospitalService hospitalService) {
        this.hospitalDataClient = hospitalDataClient;
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
        this.hospitalService = hospitalService;
    }

    @GetMapping("/data")
    public String fourthJob(@RequestParam("value") String value) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("date", value)
            .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("hospitalDataJob"), jobParameters);
        return "ok";
    }

    @GetMapping("/api/v1/hospital/search")
    public CustomApiResponse<HospitalsPageResponse> searchHospitalPage(
        @RequestParam(value = "sidoCdNm", defaultValue="") String sidoCdNm,
        @RequestParam(value = "sgguCdNm", defaultValue="") String sgguCdNm,
        @RequestParam(value = "keyWord", defaultValue="") String keyWord,
        @RequestParam(value = "page",defaultValue = "1") int page,
        @RequestParam(value ="size" ,defaultValue = "10") int size
    ){
        HospitalsPageResponse hospitalsPages = hospitalService.searchHospital(sidoCdNm,sgguCdNm,keyWord,size,page);
        return CustomApiResponse.ok(hospitalsPages);
    }

}
