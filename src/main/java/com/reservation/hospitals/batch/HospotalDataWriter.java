package com.reservation.hospitals.batch;

import com.reservation.hospitals.domain.HospStatus;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.hospitals.repository.HospitalRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HospotalDataWriter implements ItemWriter<HospitalEntity>, StepExecutionListener {
    private final HospitalRepository hospitalRepository;
    private List<HospitalEntity> newHospitalDataListEntity = new ArrayList<>();

    public HospotalDataWriter(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void write(Chunk<? extends HospitalEntity> chunk) throws Exception {
        newHospitalDataListEntity.addAll(chunk.getItems());
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        newHospitalDataListEntity.clear();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Hospotal의 상태가 OPEN인 데이터만 가져온다.
        List<HospitalEntity> beforeHospitalDataListEntity = hospitalRepository.findByHospStatus(HospStatus.OPEN);
        Set<String> beforeYkihoSet = beforeHospitalDataListEntity.stream()
            .map(HospitalEntity::getYkiho).collect(Collectors.toSet());
        Set<String> newYkihoSet = newHospitalDataListEntity.stream()
            .map(HospitalEntity::getYkiho).collect(Collectors.toSet());
        for(HospitalEntity beforeHospitalEntity : beforeHospitalDataListEntity){
            if(!newYkihoSet.contains(beforeHospitalEntity.getYkiho())){
                beforeHospitalEntity = new HospitalEntity(beforeHospitalEntity, HospStatus.CLOSE);
                hospitalRepository.save(beforeHospitalEntity);
            }
        }

        for(HospitalEntity newHospitalEntity : newHospitalDataListEntity) {
            if(!beforeYkihoSet.contains(newHospitalEntity.getYkiho())){
                hospitalRepository.save(newHospitalEntity);
            }
        }

        return null;
    }
}
