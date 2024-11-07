package com.reservation.global.security.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserAccountDto extends User {

    private final Long userId;
    private final String username;
    private final String password;
    private final String email;

    public UserAccountDto(Long userId, String username, String password,
                           String email, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
