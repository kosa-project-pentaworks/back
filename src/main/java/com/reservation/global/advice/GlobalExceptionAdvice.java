package com.reservation.global.advice;

import com.reservation.global.exception.ApplicationException;
import com.reservation.global.response.CustomApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.reservation.global.exception.ErrorCode.DEFAULT_ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(ApplicationException.class)
    protected CustomApiResponse<?> handleSecurityException(ApplicationException e) {
        log.error("error={}", e.getMessage(), e);
        return CustomApiResponse.fail(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    protected CustomApiResponse<?> handleRuntimeException(RuntimeException e) {
        log.error("error={}", e.getMessage(), e);
        return CustomApiResponse.fail(DEFAULT_ERROR, e.getMessage());
    }
}
