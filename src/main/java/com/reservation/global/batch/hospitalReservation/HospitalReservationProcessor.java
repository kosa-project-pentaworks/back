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
        System.out.println("1111111111111");
        entityManager.merge(updateHospitalReservation.getPayment());
        System.out.println("222222222222222");
        PaymentEntity updatePayment = updateHospitalReservation.getPayment().updateStatus(now);

        System.out.println("333333333333333");
        updateHospitalReservation = updateHospitalReservation.updateStatus(now, updatePayment);
        System.out.println("4444444444444");
        return updateHospitalReservation;
    }
}
