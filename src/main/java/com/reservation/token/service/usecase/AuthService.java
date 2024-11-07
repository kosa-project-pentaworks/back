package com.reservation.token.service.usecase;

import com.reservation.token.service.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    @Override
    public TokenResponse login(String email, String password) {
        return null;
    }
}
