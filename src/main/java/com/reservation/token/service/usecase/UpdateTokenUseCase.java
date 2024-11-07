package com.reservation.token.service.usecase;

import com.reservation.token.service.response.TokenResponse;

public interface UpdateTokenUseCase {

    TokenResponse updateNewToken(String userId);

    TokenResponse upsertToken(String userId);

    TokenResponse reissueToken(String accessToken, String refreshToken);
}
