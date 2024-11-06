package com.reservation.hospitals.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HospitalController {
    private final HospitalDataClient hospitalDataClient;
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    public HospitalController(HospitalDataClient hospitalDataClient, JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.hospitalDataClient = hospitalDataClient;
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @GetMapping("/data")
    public String fourthJob(@RequestParam("value") String value) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
            .addString("date", value)
            .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("hospitalDataJob"), jobParameters);
        return "ok";
    }

}
