package com.reservation.hospitals.batch;

import com.reservation.hospitals.controller.HospitalDataClient;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.hospitals.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class HospitalDataBatch {
    private final HospitalRepository hospitalRepository;
    private final HospitalDataClient hospitalDataClient;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;



    @Bean
    public Job hospitalDataJob(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return new JobBuilder("hospitalDataJob", jobRepository)
            .start(hospitalDataStep())
            .build();
    }

    @Bean
    public Step hospitalDataStep() {
        return new StepBuilder("hospitalDataStep", jobRepository)
            .<HospitalEntity, HospitalEntity> chunk(10, platformTransactionManager)
            .reader(hospitalDataReader())
            .writer(hospitalDataWriter())
            .faultTolerant()
            .retry(Exception.class)
            .retryLimit(3)
            .build();
    }

    @Bean
    public ItemReader<HospitalEntity> hospitalDataReader() {
        return new HospitalDataReader(hospitalDataClient);
    }

    @Bean
    public ItemWriter<HospitalEntity> hospitalDataWriter() {
        return new HospotalDataWriter(hospitalRepository);
    }

}
