package com.reservation.global.batch.hospitalReservation;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.domain.ReservationStatus;
import com.reservation.payment.domain.PaymentEntity;
import com.reservation.payment.domain.PaymentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class HospitalReservationProcessor implements ItemProcessor<HospitalReservationEntity, HospitalReservationEntity> {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public HospitalReservationEntity process(HospitalReservationEntity item) throws Exception {
        HospitalReservationEntity updateHospitalReservation = item;
        LocalDate now = LocalDate.now();
        entityManager.merge(updateHospitalReservation.getPayment());
        PaymentEntity updatePayment = updateHospitalReservation.getPayment().updateStatus(now);
        updateHospitalReservation = updateHospitalReservation.updateStatus(now, updatePayment);
        return updateHospitalReservation;
    }
}
