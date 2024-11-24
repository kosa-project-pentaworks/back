package com.reservation.user.repository.grade;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reservation.user.domain.UserGrade;
import com.reservation.user.domain.UserGradeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.reservation.user.domain.QUserGradeEntity.userGradeEntity;

@Repository
@RequiredArgsConstructor
public class UserGradeCustomRepositoryImpl implements UserGradeCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<UserGrade> findByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(userGradeEntity)
                .fetch()
                .stream()
                .findFirst()
                .map(UserGradeEntity::toDomain);
    }
}
