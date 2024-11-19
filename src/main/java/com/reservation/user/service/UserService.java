package com.reservation.user.service;

import com.reservation.global.exception.UserException;
import com.reservation.user.domain.UserDto;
import com.reservation.user.repository.port.FetchUserPort;
import com.reservation.user.repository.port.InsertUserPort;
import com.reservation.user.repository.port.KakaoUserPort;
import com.reservation.user.repository.request.CreateUser;
import com.reservation.user.service.command.SocialUserRegistrationCommand;
import com.reservation.user.service.command.UserRegistrationCommand;
import com.reservation.user.service.response.*;
import com.reservation.user.service.usecase.FetchUserUseCase;
import com.reservation.user.service.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, FetchUserUseCase {

    private final FetchUserPort fetchUserPort;
    private final InsertUserPort insertUserPort;
    private final KakaoUserPort kakaoUserPort;

    @Override
    public UserRegistrationResponse register(UserRegistrationCommand request) {
        Optional<UserDto> byEmail = fetchUserPort.findByEmail(request.email());
        if (byEmail.isPresent()) {
            throw new UserException.UserAlreadyExistException();
        }

        UserDto dto = insertUserPort.create(
                CreateUser.builder()
                        .username(request.username())
                        .encryptedPassword(request.encryptedPassword())
                        .email(request.email())
                        .build()
        );

        return new UserRegistrationResponse(dto.getUsername(), dto.getEmail());
    }

    @Override
    public UserRegistrationResponse registerSocialUser(SocialUserRegistrationCommand request) {
        Optional<UserDto> byProviderId = fetchUserPort.findByProviderId(request.providerId());

        if (byProviderId.isPresent()) {
            return null;
        }

        UserDto socialUser = insertUserPort.createSocialUser(request.username(), request.provider(), request.providerId());

        return new UserRegistrationResponse(socialUser.getUsername(), null);
    }

    @Override
    public SimpleUserResponse findSimpleUserByEmail(String email) {
        Optional<UserDto> byEmail = fetchUserPort.findByEmail(email);

        if (byEmail.isEmpty()) {
            throw new UserException.UserDoesNotExistException();
        }

        UserDto dto = byEmail.get();
        return new SimpleUserResponse(dto.getUsername(), dto.getEmail());
    }

    @Override
    public DetailUserResponse findDetailUserByEmail(String email) {
        Optional<UserDto> byEmail = fetchUserPort.findByEmail(email);

        if (byEmail.isEmpty()) {
            throw new UserException.UserDoesNotExistException();
        }

        UserDto dto = byEmail.get();

        return DetailUserResponse
                .builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getEncryptedPassword())
                .build();
    }

    @Override
    public UserResponse findByProviderId(String providerId) {
        return fetchUserPort.findByProviderId(providerId)
                .map(UserResponse::toUserResponse)
                .orElse(null);
    }

    @Override
    public DetailSocialUserResponse findDetailSocialUserByProviderId(String providerId) {
        Optional<UserDto> byProviderId = fetchUserPort.findByProviderId(providerId);

        if (byProviderId.isEmpty()) {
            throw new UserException.UserDoesNotExistException();
        }

        UserDto dto = byProviderId.get();

        return DetailSocialUserResponse
                .builder()
                .socialUserId(dto.getUserId())
                .username(dto.getUsername())
                .provider(dto.getProvider())
                .providerId(dto.getProviderId())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    @Override
    public SocialUserResponse findKakaoUser(String accessToken) {
        UserDto userFromKakao = kakaoUserPort.findUserFromKakao(accessToken);
        return new SocialUserResponse(
                userFromKakao.getUsername(), "kakao", userFromKakao.getProviderId()
        );
    }
}
