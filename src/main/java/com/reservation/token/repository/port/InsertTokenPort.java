package com.reservation.token.repository.port;

import com.reservation.token.domain.TokenDto;

public interface InsertTokenPort {

    TokenDto create(String userId, String accessToken, String refreshToken);
}
