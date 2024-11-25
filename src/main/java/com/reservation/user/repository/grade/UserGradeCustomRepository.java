package com.reservation.user.repository.grade;

import com.reservation.user.domain.UserGrade;

import java.util.Optional;

public interface UserGradeCustomRepository {

    Optional<UserGrade> findByUserId(Long userId);
}
