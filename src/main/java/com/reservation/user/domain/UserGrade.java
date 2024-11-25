package com.reservation.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGrade {

    private Long userId;
    private GradeType gradeType;
    private Boolean validYn;

    public UserGrade(Long userId, GradeType gradeType, Boolean validYn) {
        this.userId = userId;
        this.gradeType = gradeType;
        this.validYn = true;
    }

    public void off() {
        this.validYn = false;
    }

    public void on(GradeType gradeType) {
        if (this.validYn) {
            return;
        }

        this.gradeType = gradeType;
        this.validYn = true;
    }

    public void change(GradeType type) {
        this.gradeType = type;
    }

    public static UserGrade newGrade(Long userId, GradeType gradeType) {
        return new UserGrade(
                userId,
                gradeType,
                true
        );
    }
}
