package com.reservation.user.repository.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUser {

    private final String username;
    private final String encryptedPassword;
    private final String email;

    public CreateUser(String username, String encryptedPassword, String email) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.email = email;
    }
}
