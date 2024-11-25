package com.reservation.global.exception;

import static com.reservation.global.exception.ErrorCode.HOSPITAL_RESERVATION_DOES_NOT_EXIT;

public class HospitalReservationException extends ApplicationException {
    public HospitalReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
    public static class  HospitalReservationDoesNotExit extends HospitalReservationException{

        public HospitalReservationDoesNotExit() {
            super(HOSPITAL_RESERVATION_DOES_NOT_EXIT);
        }
    }

}
