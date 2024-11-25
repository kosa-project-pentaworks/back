package com.reservation.global.batch.hospitalReservation;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.repository.HospitalReservationRepository;
import org.springframework.batch.item.*;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

public class HospitalReservationReader implements ItemStreamReader<HospitalReservationEntity> {
    private final HospitalReservationRepository hospitalReservationRepository;
    private Iterator<HospitalReservationEntity> hospitalReservationIterator;
    public HospitalReservationReader(HospitalReservationRepository hospitalReservationRepository) {
        this.hospitalReservationRepository = hospitalReservationRepository;
    }

    @Override
    public HospitalReservationEntity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(hospitalReservationIterator != null && hospitalReservationIterator.hasNext()){
            return hospitalReservationIterator.next();
        }else{
            return null;
        }

    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        LocalDate newDate = LocalDate.now();
        newDate = newDate.plusDays(1);
        List<HospitalReservationEntity> hospitalReservationList = hospitalReservationRepository.findAllByReservationAt(newDate);
        this.hospitalReservationIterator = hospitalReservationList.iterator();

    }
}
