package com.reservation.hospitals.repository.custom;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reservation.hospitalReviews.domain.QHospitalReviewEntity;
import com.reservation.hospitals.domain.HospStatus;

import com.reservation.hospitals.domain.QHospitalAdmin;
import com.reservation.hospitals.domain.dto.HospitalSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.reservation.hospitalReviews.domain.QHospitalReviewEntity.hospitalReviewEntity;
import static com.reservation.hospitals.domain.QHospitalAdmin.hospitalAdmin;
import static com.reservation.hospitals.domain.QHospitalEntity.*;


@Repository
@RequiredArgsConstructor
public class HospitalRepositoryImpl implements HospitalRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public PageImpl<HospitalSearchDto> findHospitalAll(String sidoCdNm, String sgguCdNm, String keyword, Pageable pageable) {
        QueryResults<Long> hospitalIds =jpaQueryFactory
            .select(hospitalEntity.hospId)
            .from(hospitalEntity)
            .where(
                sidoCdNmEq(sidoCdNm),
                sgguCdNmEq(sgguCdNm),
                keywordEq(keyword),
                hospitalEntity.hospStatus.eq(HospStatus.OPEN)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<HospitalSearchDto> hospitalSearchList = jpaQueryFactory
            .select(Projections.constructor(HospitalSearchDto.class,
                hospitalEntity.hospId,
                hospitalEntity.addr,
                hospitalEntity.yadmNm,
                hospitalEntity.telno,
                hospitalReviewEntity.count().as("reviewCount"),
                hospitalReviewEntity.hospReviewRating.avg().round().as("ratingAvg")
            )).from(hospitalEntity)
            .leftJoin(hospitalEntity.hospitalReviewEntity, hospitalReviewEntity)
            .where(hospitalEntity.hospId.in(hospitalIds.getResults()))
            .groupBy(hospitalEntity.hospId,
                hospitalEntity.addr,
                hospitalEntity.yadmNm,
                hospitalEntity.telno)
            .fetch();
        return new PageImpl<>(hospitalSearchList, pageable, hospitalIds.getTotal());
    }


    private BooleanExpression sidoCdNmEq(String sidoCdNm) {
        return StringUtils.hasText(sidoCdNm) ? hospitalEntity.sidoCdNm.eq(sidoCdNm) : null;
    }

    private BooleanExpression sgguCdNmEq(String sgguCdNm) {
        return StringUtils.hasText(sgguCdNm) ? hospitalEntity.sgguCdNm.eq(sgguCdNm) : null;
    }

    private BooleanExpression keywordEq(String keyword) {
        return StringUtils.hasText(keyword) ? hospitalEntity.yadmNm.like("%" + keyword +"%") : null;

    }
}
