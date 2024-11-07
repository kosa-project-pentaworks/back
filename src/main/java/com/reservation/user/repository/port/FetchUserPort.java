package com.reservation.user.repository.port;

import com.reservation.user.domain.UserDto;

import java.util.Optional;

public interface FetchUserPort {

    Optional<UserDto> findByEmail(String email);

    UserDto getByEmail(String email);

    Optional<UserDto> findByProviderId(String providerId);
}
