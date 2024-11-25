package com.reservation.user.controller;

import com.reservation.global.response.CustomApiResponse;
import com.reservation.global.security.domain.UserAccountDto;
import com.reservation.token.service.response.TokenResponse;
import com.reservation.token.service.usecase.FetchTokenUseCase;
import com.reservation.token.service.usecase.UpdateTokenUseCase;
import com.reservation.user.controller.request.LoginRequest;
import com.reservation.user.service.command.SocialUserRegistrationCommand;
import com.reservation.user.service.response.SocialUserResponse;
import com.reservation.user.service.response.UserResponse;
import com.reservation.user.service.usecase.FetchUserUseCase;
import com.reservation.user.service.usecase.RegisterUserUseCase;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.reservation.global.exception.ErrorCode.DEFAULT_ERROR;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UpdateTokenUseCase updateTokenUseCase;
    private final FetchTokenUseCase fetchTokenUseCase;
    private final FetchUserUseCase fetchUserUseCase;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/login")
    public CustomApiResponse<TokenResponse> login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        UserAccountDto principal = (UserAccountDto) authentication.getPrincipal();

        TokenResponse tokenResponse = updateTokenUseCase.upsertToken(principal.getEmail());

        return CustomApiResponse.ok(tokenResponse);
    }

    @PostMapping("/reissue")
    public CustomApiResponse<TokenResponse> reissue(HttpServletRequest httpServletRequest) {
        String refreshToken = httpServletRequest.getHeader("refresh_token");
        String accessToken = httpServletRequest.getHeader("token");

        if (StringUtils.isBlank(refreshToken) || StringUtils.isBlank(accessToken)) {
            return CustomApiResponse.fail(DEFAULT_ERROR, "토큰이 없습니다.");
        }

        return CustomApiResponse.ok(updateTokenUseCase.reissueToken(accessToken, refreshToken));
    }

    @PostMapping("/callback")
    public CustomApiResponse<TokenResponse> kakaoCallback(@RequestBody Map<String, String> request) {
        String code = request.get("code");

        String tokenFromKakao = fetchTokenUseCase.getTokenFromKakao(code);
        SocialUserResponse kakaoUser = fetchUserUseCase.findKakaoUser(tokenFromKakao);

        UserResponse byProviderId = fetchUserUseCase.findByProviderId(kakaoUser.providerId());
        if (ObjectUtils.isEmpty(byProviderId)) {
            registerUserUseCase.registerSocialUser(new SocialUserRegistrationCommand(
                    kakaoUser.name(),
                    kakaoUser.provider(),
                    kakaoUser.providerId()
            ));
        }

        return CustomApiResponse.ok(updateTokenUseCase.upsertToken(kakaoUser.providerId()));
    }
}
