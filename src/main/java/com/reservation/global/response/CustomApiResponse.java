package com.reservation.global.response;

import com.reservation.global.exception.ErrorCode;

public record CustomApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) {

    public static final String CODE_SUCCEED = "SUCCEED";

    public static <T> CustomApiResponse<T> ok(T data) {
        return new CustomApiResponse<>(true, CODE_SUCCEED, null, data);
    }

    public static <T> CustomApiResponse<T> fail(ErrorCode errorCode, String message) {
        return new CustomApiResponse<>(false, errorCode.toString(), message, null);
    }
}
