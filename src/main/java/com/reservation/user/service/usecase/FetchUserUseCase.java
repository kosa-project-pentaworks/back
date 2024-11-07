package com.reservation.user.service.usecase;

import com.reservation.user.service.response.DetailUserResponse;
import com.reservation.user.service.response.SimpleUserResponse;
import com.reservation.user.service.response.SocialUserResponse;
import com.reservation.user.service.response.UserResponse;

public interface FetchUserUseCase {

    SimpleUserResponse findSimpleUserByEmail(String email);

    DetailUserResponse findDetailUserByEmail(String email);

    UserResponse findByProviderId(String providerId);

    SocialUserResponse findKakaoUser(String accessToken);
}
