package com.reservation.global.exception;

import static com.reservation.global.exception.ErrorCode.USER_ALREADY_EXIST;
import static com.reservation.global.exception.ErrorCode.USER_DOES_NOT_EXIST;

public class UserException extends ApplicationException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class UserDoesNotExistException extends UserException {
        public UserDoesNotExistException() {
            super(USER_DOES_NOT_EXIST);
        }
    }

    public static class UserAlreadyExistException extends UserException {
        public UserAlreadyExistException() {
            super(USER_ALREADY_EXIST);
        }
    }
}
