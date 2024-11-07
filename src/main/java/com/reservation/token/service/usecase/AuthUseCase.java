package com.reservation.token.service.usecase;

import com.reservation.token.service.response.TokenResponse;

public interface AuthUseCase {

    TokenResponse login(String email, String password);
}
