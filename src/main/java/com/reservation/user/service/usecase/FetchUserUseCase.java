package com.reservation.user.service.usecase;

import com.reservation.user.service.response.*;

public interface FetchUserUseCase {

    SimpleUserResponse findSimpleUserByEmail(String email);

    DetailUserResponse findDetailUserByEmail(String email);

    UserResponse findByProviderId(String providerId);

    DetailSocialUserResponse findDetailSocialUserByProviderId(String providerId);

    SocialUserResponse findKakaoUser(String accessToken);
}
