package com.reservation.global.security.service;

import com.reservation.global.security.domain.UserAccountDto;
import com.reservation.user.service.response.DetailUserResponse;
import com.reservation.user.service.usecase.FetchUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final FetchUserUseCase fetchUserUseCase;

    @Override
    public UserAccountDto loadUserByUsername(String email) throws UsernameNotFoundException {
        DetailUserResponse user = fetchUserUseCase.findDetailUserByEmail(email);

        return new UserAccountDto(
                user.userId(), user.username(), user.password(),
                user.email(), List.of()
        );
    }
}
