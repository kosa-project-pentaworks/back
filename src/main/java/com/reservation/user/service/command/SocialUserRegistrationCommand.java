package com.reservation.user.service.command;

import lombok.Builder;

@Builder
public record SocialUserRegistrationCommand(String username, String provider, String providerId) {
}
