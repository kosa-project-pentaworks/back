package com.reservation.user.repository.social;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reservation.user.domain.QSocialUserEntity;
import com.reservation.user.domain.SocialUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.reservation.user.domain.QSocialUserEntity.*;

@Repository
@RequiredArgsConstructor
public class SocialUserCustomRepositoryImpl implements SocialUserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<SocialUserEntity> findByProviderId(String providerId) {
        return jpaQueryFactory.selectFrom(socialUserEntity)
                .where(socialUserEntity.providerId.eq(providerId))
                .fetch()
                .stream().findFirst();
    }
}
