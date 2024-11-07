package com.reservation.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reservation.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.reservation.user.domain.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return jpaQueryFactory.selectFrom(userEntity)
                .where(userEntity.email.eq(email))
                .fetch()
                .stream().findFirst();
    }
}
