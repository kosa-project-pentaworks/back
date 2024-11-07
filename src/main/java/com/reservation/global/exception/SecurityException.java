package com.reservation.global.exception;

public class SecurityException extends ApplicationException {

    public SecurityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class PasswordEncryptionException extends SecurityException {
        public PasswordEncryptionException() {
            super(ErrorCode.PASSWORD_ENCRYPTION_FAILED);
        }
    }
}
