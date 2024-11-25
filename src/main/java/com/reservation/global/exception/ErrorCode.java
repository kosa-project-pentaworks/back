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
    HOSPITAL_RESERVATION_ALREADY_EXIT("ERROR_4001","예약된 병원이 있습니다.."),
    PAYMENT_DOES_NOT_EXIT("ERROR_5000", "결제 정보가 없습니다."),
    PAYMENT_ALREADY_EXIT("ERROR_5001", "결제 정보가 있습니다."),
    HOSPITAL_DOES_NOT_EXIT("ERROR_6000","병원이 존재하지 않습니다."),
    REDIS_ALREADY_EXIT("ERROR_7001","이미 해당 예약은 결제 진행중입니다."),
    HOSPITAL_REVIEW_DOES_NOT_EXIT("ERROR_8000", "해당 리뷰가 없습니다."),
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
