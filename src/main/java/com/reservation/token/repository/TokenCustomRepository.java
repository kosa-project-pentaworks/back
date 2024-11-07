package com.reservation.token.repository;

import com.reservation.token.domain.TokenEntity;

import java.util.Optional;

public interface TokenCustomRepository {

    Optional<TokenEntity> findByUserId(String userId);
}
