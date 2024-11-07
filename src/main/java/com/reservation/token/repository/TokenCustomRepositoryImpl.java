package com.reservation.token.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reservation.token.domain.TokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.reservation.token.domain.QTokenEntity.tokenEntity;

@Repository
@RequiredArgsConstructor
public class TokenCustomRepositoryImpl implements TokenCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TokenEntity> findByUserId(String userId) {
        return jpaQueryFactory.selectFrom(tokenEntity)
                .where(tokenEntity.userId.eq(userId))
                .fetch()
                .stream().findFirst();
    }
}
