package com.reservation.user.service.response;

import com.reservation.user.domain.Address;

public record SocialUserModificationResponse(String providerId, String phone, Address address) {
}
