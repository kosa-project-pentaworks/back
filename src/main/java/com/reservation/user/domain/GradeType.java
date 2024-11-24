package com.reservation.user.domain;

import lombok.Getter;

@Getter
public enum GradeType {
    BASIC("일반 회원"),
    SPECIAL("보훈자"),
    ADMIN("관리자"),
    ;

    private final String desc;

    GradeType(String desc) {
        this.desc = desc;
    }

    public String toRole() {
        return "ROLE_" + this.name();
    }
}
