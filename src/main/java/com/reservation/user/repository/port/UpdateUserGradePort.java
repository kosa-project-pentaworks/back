package com.reservation.user.repository.port;

import com.reservation.user.domain.UserGrade;

public interface UpdateUserGradePort {

    UserGrade update(UserGrade userGrade);
}
