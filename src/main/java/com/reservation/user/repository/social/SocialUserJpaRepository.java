package com.reservation.user.repository.social;

import com.reservation.user.domain.SocialUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialUserJpaRepository extends JpaRepository<SocialUserEntity, String>, SocialUserCustomRepository {

    void deleteByProviderId(String providerId);
}
