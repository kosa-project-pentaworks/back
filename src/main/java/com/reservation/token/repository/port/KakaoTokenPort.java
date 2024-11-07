package com.reservation.token.repository.port;

public interface KakaoTokenPort {

    String getAccessTokenByCode(String code);
}
