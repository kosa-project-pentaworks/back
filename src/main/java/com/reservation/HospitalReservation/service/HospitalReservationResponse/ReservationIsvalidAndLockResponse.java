package com.reservation.HospitalReservation.service.HospitalReservationResponse;

import lombok.Getter;

@Getter
public class ReservationIsvalidAndLockResponse {
    private final String redisKey;
    private final Boolean isvalid;

    public ReservationIsvalidAndLockResponse(String redisKey, Boolean isvalid) {
        this.redisKey = redisKey;
        this.isvalid = isvalid;
    }
}
