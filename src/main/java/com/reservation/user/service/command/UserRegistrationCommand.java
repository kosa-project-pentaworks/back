package com.reservation.user.service.command;

import lombok.Builder;

@Builder
public record UserRegistrationCommand(String username, String encryptedPassword, String email) {
}
