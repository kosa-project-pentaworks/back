package com.reservation.user.repository.port;

import com.reservation.user.domain.UserDto;
import com.reservation.user.repository.request.CreateUser;

public interface InsertUserPort {

    UserDto create(CreateUser create);

    UserDto createSocialUser(String username, String provider, String providerId);
}
