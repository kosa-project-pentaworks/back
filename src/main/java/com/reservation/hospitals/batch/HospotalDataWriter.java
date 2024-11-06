package com.reservation.hospitals.batch;

import com.reservation.hospitals.domain.HospStatus;
import com.reservation.hospitals.domain.Hospital;
import com.reservation.hospitals.repository.HospitalRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HospotalDataWriter implements ItemWriter<Hospital>, StepExecutionListener {
    private final HospitalRepository hospitalRepository;
    private List<Hospital> newHospitalDataList = new ArrayList<>();

    public HospotalDataWriter(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void write(Chunk<? extends Hospital> chunk) throws Exception {
        newHospitalDataList.addAll(chunk.getItems());
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        newHospitalDataList.clear();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Hospotal의 상태가 OPEN인 데이터만 가져온다.
        List<Hospital> beforeHospitalDataList = hospitalRepository.findByHospStatus(HospStatus.OPEN);
        Set<String> beforeYkihoSet = beforeHospitalDataList.stream()
            .map(Hospital::getYkiho).collect(Collectors.toSet());
        Set<String> newYkihoSet = newHospitalDataList.stream()
            .map(Hospital::getYkiho).collect(Collectors.toSet());
        for(Hospital beforeHospital : beforeHospitalDataList){
            if(!newYkihoSet.contains(beforeHospital.getYkiho())){
                beforeHospital = new Hospital(beforeHospital, HospStatus.CLOSE);
                hospitalRepository.save(beforeHospital);
            }
        }

        for(Hospital newHospital : newHospitalDataList) {
            if(!beforeYkihoSet.contains(newHospital.getYkiho())){
                hospitalRepository.save(newHospital);
            }
        }

        return null;
    }
}
