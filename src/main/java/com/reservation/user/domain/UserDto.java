package com.reservation.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private final Long userId;
    private final String username;
    private final String encryptedPassword;
    private final String email;
    private final String provider;
    private final String providerId;
    private final String role;
    private final String phone;
    private final Address address;

    public UserDto(Long userId, String username, String encryptedPassword, String email, String provider, String providerId, String role, String phone, Address address) {
        this.userId = userId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.phone = phone;
        this.address = address;
    }
}
