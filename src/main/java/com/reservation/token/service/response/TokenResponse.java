package com.reservation.token.service.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record TokenResponse(String accessToken, String refreshToken) {
}
