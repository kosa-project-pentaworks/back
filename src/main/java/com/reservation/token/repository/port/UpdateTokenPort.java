package com.reservation.token.repository.port;

public interface UpdateTokenPort {

    void updateToken(String userId, String accessToken, String refreshToken);
}
