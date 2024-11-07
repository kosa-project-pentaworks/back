package com.reservation.hospitals.batch;

import com.reservation.hospitals.controller.HospitalDataClient;
import com.reservation.hospitals.domain.Hospital;
import com.reservation.hospitals.repository.HospitalRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class HospitalDataBatch {
    private final HospitalRepository hospitalRepository;
    private final HospitalDataClient hospitalDataClient;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    public HospitalDataBatch(HospitalRepository hospitalRepository, HospitalDataClient hospitalDataClient, JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalDataClient = hospitalDataClient;
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public Job hospitalDataJob(){
        return new JobBuilder("hospitalDataJob", jobRepository)
            .start(hospitalDataStep())
            .build();
    }

    @Bean
    public Step hospitalDataStep() {
        return new StepBuilder("hospitalDataStep", jobRepository)
            .<Hospital, Hospital> chunk(10, platformTransactionManager)
            .reader(hospitalDataReader())
            .writer(hospitalDataWriter())
            .faultTolerant()
            .retry(Exception.class)
            .retryLimit(3)
            .build();
    }

    @Bean
    public ItemReader<Hospital> hospitalDataReader() {
        return new HospitalDataReader(hospitalDataClient);
    }

    @Bean
    public ItemWriter<Hospital> hospitalDataWriter() {
        return new HospotalDataWriter(hospitalRepository);
    }

}
