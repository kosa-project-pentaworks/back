package com.reservation.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DEFAULT_ERROR("ERROR_0000", "에러가 발생하였습니다."),

    PASSWORD_ENCRYPTION_FAILED("ERROR_1000", "비밀번호 암호화 중 에러가 발생했습니다."),

    USER_DOES_NOT_EXIST("ERROR_2000", "사용자가 존재하지 않습니다."),
    USER_ALREADY_EXIST("ERROR_2001", "사용자가 이미 존재합니다."),
    HOSPITAL_RESERVATION_DOES_NOT_EXIT("ERROR_4000","예약된 병원이 없습니다."),
    ;

    private final String code;
    private final String desc;

    ErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return code + ": " + desc;
    }
}
