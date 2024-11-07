package com.reservation.user.service.response;

import com.reservation.user.domain.UserDto;
import org.apache.commons.lang3.StringUtils;

public record UserResponse(
        Long userId,
        String username,
        String password,
        String email,
        String provider,
        String providerId,
        String role
) {
    public static UserResponse toUserResponse(UserDto dto) {
        return new UserResponse(
                dto.getUserId(),
                dto.getUsername(),
                StringUtils.defaultIfBlank(dto.getEncryptedPassword(), "password"),
                StringUtils.defaultIfBlank(dto.getEmail(), "email@email.com"),
                StringUtils.defaultIfBlank(dto.getProvider(), "provider"),
                StringUtils.defaultIfBlank(dto.getProviderId(), "provider-id"),
                dto.getRole()
        );
    }
}
