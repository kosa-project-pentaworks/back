package com.reservation.global.batch.hospitalReservation;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.repository.HospitalReservationRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class HospitalReservationWriter implements ItemWriter<HospitalReservationEntity> {
    private final HospitalReservationRepository hospitalReservationRepository;
    public HospitalReservationWriter(HospitalReservationRepository hospitalReservationRepository) {
        this.hospitalReservationRepository = hospitalReservationRepository;
    }

    @Override
    public void write(Chunk<? extends HospitalReservationEntity> chunk) throws Exception {
        chunk.getItems().forEach(hospitalReservationRepository::save);
    }
}
