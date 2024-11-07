package com.reservation.user.repository.social;

import com.reservation.user.domain.SocialUserEntity;

import java.util.Optional;

public interface SocialUserCustomRepository {

    Optional<SocialUserEntity> findByProviderId(String providerId);
}
