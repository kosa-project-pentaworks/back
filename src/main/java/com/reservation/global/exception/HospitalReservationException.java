package com.reservation.global.exception;

public class HospitalReservationException extends ApplicationException{
    public HospitalReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
