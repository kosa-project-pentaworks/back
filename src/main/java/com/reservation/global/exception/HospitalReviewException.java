package com.reservation.global.exception;

import static com.reservation.global.exception.ErrorCode.HOSPITAL_REVIEW_DOES_NOT_EXIT;

public class HospitalReviewException extends ApplicationException {
    public HospitalReviewException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class HospitalReviewDoesNotExit extends HospitalReviewException{

        public HospitalReviewDoesNotExit() {
            super(HOSPITAL_REVIEW_DOES_NOT_EXIT);
        }
    }
}
