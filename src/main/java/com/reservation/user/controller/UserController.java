package com.reservation.user.controller;

import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;
import com.reservation.global.response.CustomApiResponse;
import com.reservation.user.controller.request.UserRegistrationRequest;
import com.reservation.user.service.command.UserRegistrationCommand;
import com.reservation.user.service.response.*;
import com.reservation.user.service.usecase.FetchUserUseCase;
import com.reservation.user.service.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final FetchUserUseCase fetchUserUseCase;

    @GetMapping("/{providerId}")
    public CustomApiResponse<DetailSocialUserResponse> findSocialUserByProviderId(@PathVariable String providerId) {
        return CustomApiResponse.ok(fetchUserUseCase.findDetailSocialUserByProviderId(providerId));
    }

    @PostMapping("/register")
    public CustomApiResponse<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest request) {
        UserRegistrationCommand command = UserRegistrationCommand.builder()
                .username(request.getUsername())
                .encryptedPassword(request.getPassword())
                .email(request.getEmail())
                .build();
        return CustomApiResponse.ok(registerUserUseCase.register(command));
    }
}
