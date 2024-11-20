package com.reservation.hospitalReviews.repository.custom;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reservation.HospitalReservation.domain.QHospitalReservationEntity;
import com.reservation.hospitalReviews.domain.QHospitalReviewEntity;
import com.reservation.hospitalReviews.repository.custom.dto.FindHospitalReviewDto;
import com.reservation.hospitals.domain.QHospitalEntity;
import com.reservation.user.domain.QSocialUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.reservation.HospitalReservation.domain.QHospitalReservationEntity.hospitalReservationEntity;
import static com.reservation.hospitalReviews.domain.QHospitalReviewEntity.hospitalReviewEntity;
import static com.reservation.user.domain.QSocialUserEntity.socialUserEntity;

@Repository
@RequiredArgsConstructor
public class HospitalReviewRepositoryImpl implements HospitalReviewRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PageImpl<FindHospitalReviewDto> findReviewByHospitalId(Long hospId, Pageable pageable) {
        System.out.println("들어오는 병원 ==>>" + hospId);

        QueryResults<Long> reviewIds = jpaQueryFactory.select(hospitalReviewEntity.hospReviewId)
            .from(hospitalReviewEntity)
            .where(hospitalReviewEntity.hospitalEntity.hospId.eq(hospId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<FindHospitalReviewDto> result = jpaQueryFactory.select(
                Projections.constructor(FindHospitalReviewDto.class,
                    hospitalReviewEntity.userEntity.username,
                    hospitalReviewEntity.hospReviewContent,
                    hospitalReviewEntity.hospReviewRating,
                    hospitalReviewEntity.hospitalReservation.reservationAt,
                    hospitalReviewEntity.hospitalReservation.reservationTime
                    )
            ).from(hospitalReviewEntity)
            .leftJoin(hospitalReviewEntity.userEntity, socialUserEntity)
            .leftJoin(hospitalReviewEntity.hospitalReservation, hospitalReservationEntity)
            .where(hospitalReviewEntity.hospReviewId.in(reviewIds.getResults()))
            .orderBy(hospitalReviewEntity.hospReviewId.desc())
            .fetch();

        return new PageImpl<>(result, pageable, reviewIds.getTotal());
    }
}
