package com.reservation.payment.repository;

import com.reservation.payment.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
