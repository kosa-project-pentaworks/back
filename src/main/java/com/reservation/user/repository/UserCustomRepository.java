package com.reservation.user.repository;

import com.reservation.user.domain.UserEntity;

import java.util.Optional;

public interface UserCustomRepository {

    Optional<UserEntity> findByEmail(String email);
}
