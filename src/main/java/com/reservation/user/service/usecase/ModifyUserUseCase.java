package com.reservation.user.service.usecase;

import com.reservation.user.service.command.SocialUserModificationCommand;
import com.reservation.user.service.response.SocialUserModificationResponse;

public interface ModifyUserUseCase {

    SocialUserModificationResponse modify(SocialUserModificationCommand request);
}
