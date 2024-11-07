package com.reservation.global.security.filter;

import com.reservation.token.service.usecase.FetchTokenUseCase;
import com.reservation.user.service.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final FetchTokenUseCase fetchTokenUseCase;

    public Authentication getAuthentication(String accessToken) {
        UserResponse userResponse = fetchTokenUseCase.findUserByAccessToken(accessToken);
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority(userResponse.role()));
        UserDetails principal = new User(userResponse.username(), userResponse.password(), simpleGrantedAuthorities);

        return new UsernamePasswordAuthenticationToken(principal, userResponse.userId(), simpleGrantedAuthorities);
    }

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getCredentials();
    }

    public String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getAuthorities().stream().findFirst().orElseThrow(RuntimeException::new).getAuthority();
    }
}
