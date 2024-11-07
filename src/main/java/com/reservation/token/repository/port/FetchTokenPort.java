package com.reservation.token.repository.port;

import com.reservation.token.domain.TokenDto;

import java.util.Optional;

public interface FetchTokenPort {

    Optional<TokenDto> findByUserId(String userId);
}
