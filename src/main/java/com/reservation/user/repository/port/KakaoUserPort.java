package com.reservation.user.repository.port;

import com.reservation.user.domain.UserDto;

public interface KakaoUserPort {

    UserDto findUserFromKakao(String accessToken);
}
