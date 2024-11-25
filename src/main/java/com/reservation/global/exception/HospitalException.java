package com.reservation.global.exception;

import static com.reservation.global.exception.ErrorCode.HOSPITAL_DOES_NOT_EXIT;

public class HospitalException extends ApplicationException{
    public HospitalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class HospitalDoesNotExitException extends HospitalException{
        public HospitalDoesNotExitException() {
            super(HOSPITAL_DOES_NOT_EXIT);
        }
    }
}
