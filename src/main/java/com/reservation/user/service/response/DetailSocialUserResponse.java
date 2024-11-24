package com.reservation.user.service.response;

import com.reservation.user.domain.Address;
import lombok.Builder;

@Builder
public record DetailSocialUserResponse(
        Long socialUserId,
        String username,
        String provider,
        String providerId,
        String phone,
        Address address,
        String role
) {
}
