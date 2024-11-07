package com.reservation.token.service.usecase;

import com.reservation.token.service.response.TokenResponse;
import com.reservation.user.service.response.UserResponse;

public interface FetchTokenUseCase {

    TokenResponse findTokenByUserId(String userId);

    UserResponse findUserByAccessToken(String accessToken);

    Boolean validateToken(String accessToken);

    String getTokenFromKakao(String code);
}
