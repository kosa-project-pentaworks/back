package com.reservation.user.service.usecase;

import com.reservation.user.service.command.SocialUserRegistrationCommand;
import com.reservation.user.service.command.UserRegistrationCommand;
import com.reservation.user.service.response.UserRegistrationResponse;

public interface RegisterUserUseCase {

    UserRegistrationResponse register(UserRegistrationCommand request);

    UserRegistrationResponse registerSocialUser(SocialUserRegistrationCommand request);
}
