package com.reservation.HospitalReservation.repository.custom.dto;

import lombok.Getter;

@Getter
public class FindOneHospitalReservationPaymentDto {
    private Long paymentId;
    private String impUid;
    private int amount;

    public FindOneHospitalReservationPaymentDto(Long paymentId, String impUid, int amount) {
        this.paymentId = paymentId;
        this.impUid = impUid;
        this.amount = amount;
    }
}
