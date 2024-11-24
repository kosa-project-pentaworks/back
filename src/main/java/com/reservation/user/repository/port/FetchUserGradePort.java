package com.reservation.user.repository.port;

import com.reservation.user.domain.UserGrade;

import java.util.Optional;

public interface FetchUserGradePort {

    Optional<UserGrade> findByUserId(Long userId);
}
