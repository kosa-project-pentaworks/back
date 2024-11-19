package com.reservation.global.batch.hospitalReservation;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.repository.HospitalReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class HospitalReservationJob {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final HospitalReservationRepository hospitalReservationRepository;
    @Bean
    public Job hospitalReservationJob1(){
        return new JobBuilder("hospitalReservationJob", jobRepository)
            .start(hospitalReservationStep())
            .build();
    }

    @Bean
    public Step hospitalReservationStep(){
        return new StepBuilder("hospitalReservationStep", jobRepository)
            .<HospitalReservationEntity, HospitalReservationEntity> chunk(10, platformTransactionManager)
            .reader(hospitalReservationReader())
            .processor(hosppitalResrvationProcess())
            .writer(hospitalReservationEntityWriter())
            .build();
    }
    @Bean
    public ItemReader<HospitalReservationEntity> hospitalReservationReader(){
        return new HospitalReservationReader(hospitalReservationRepository);
    }
    @Bean
    public ItemProcessor<HospitalReservationEntity, HospitalReservationEntity> hosppitalResrvationProcess(){
        return new HospitalReservationProcessor();
    }
    @Bean
    public ItemWriter<HospitalReservationEntity> hospitalReservationEntityWriter(){
        return new HospitalReservationWriter(hospitalReservationRepository);
    }

}
