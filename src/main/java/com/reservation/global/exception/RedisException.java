package com.reservation.global.exception;

import static com.reservation.global.exception.ErrorCode.REDIS_ALREADY_EXIT;

public class RedisException extends ApplicationException{
    public RedisException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static class RedisAlreadyExitException extends RedisException {
        public RedisAlreadyExitException() {
            super(REDIS_ALREADY_EXIT);
        }

    }
}
