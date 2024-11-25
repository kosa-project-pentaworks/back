package com.reservation.user.repository.port;

import com.reservation.user.domain.GradeType;
import com.reservation.user.domain.UserGrade;

public interface InsertUserGradePort {

    UserGrade create(Long userId, GradeType gradeType);
}
