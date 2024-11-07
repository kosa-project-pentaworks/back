package com.reservation.user.service.response;

public record SocialUserResponse(
        String name,
        String provider,
        String providerId
) {
}
