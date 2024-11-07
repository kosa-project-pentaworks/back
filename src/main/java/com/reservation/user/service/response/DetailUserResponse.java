package com.reservation.user.service.response;

import lombok.Builder;

@Builder
public record DetailUserResponse(
        Long userId,
        String username,
        String password,
        String email
) {
}
