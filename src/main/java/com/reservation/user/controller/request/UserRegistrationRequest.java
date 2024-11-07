package com.reservation.user.controller.request;

import com.reservation.global.annotation.PasswordEncryption;
import lombok.Getter;

@Getter
public class UserRegistrationRequest {

    private final String username;

    @PasswordEncryption
    private String password;

    private final String email;

    public UserRegistrationRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
