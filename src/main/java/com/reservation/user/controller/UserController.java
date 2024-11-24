package com.reservation.user.controller;

import com.reservation.global.response.CustomApiResponse;
import com.reservation.global.security.filter.JwtTokenProvider;
import com.reservation.user.controller.request.UserModificationRequest;
import com.reservation.user.controller.request.UserRegistrationRequest;
import com.reservation.user.service.command.SocialUserModificationCommand;
import com.reservation.user.service.command.UserRegistrationCommand;
import com.reservation.user.service.response.DetailSocialUserResponse;
import com.reservation.user.service.response.SocialUserModificationResponse;
import com.reservation.user.service.response.UserRegistrationResponse;
import com.reservation.user.service.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final FetchUserUseCase fetchUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final ModifyUserUseCase modifyUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GradeUseCase gradeUseCase;

    private final JwtTokenProvider jwtTokenProvider;

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

    @PutMapping("/{providerId}")
    public CustomApiResponse<SocialUserModificationResponse> modify(@PathVariable String providerId, @RequestBody UserModificationRequest request) {
        SocialUserModificationCommand command = SocialUserModificationCommand.builder()
                .providerId(providerId)
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        return CustomApiResponse.ok(modifyUserUseCase.modify(command));
    }

    @DeleteMapping("/{providerId}")
    public CustomApiResponse<String> deleteUser(@PathVariable String providerId) {
        deleteUserUseCase.deleteByProviderId(providerId);
        return CustomApiResponse.ok("Deleted Successfully");
    }
}
