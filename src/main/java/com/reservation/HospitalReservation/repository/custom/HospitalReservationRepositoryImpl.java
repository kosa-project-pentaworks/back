package com.reservation.HospitalReservation.repository.custom;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reservation.HospitalReservation.controller.request.ReservationSaveRequest;
import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.domain.ReservationStatus;
import com.reservation.HospitalReservation.repository.custom.dto.FindHospitalReservationDto;
import com.reservation.HospitalReservation.repository.custom.dto.FindOneHospitalReservationPaymentDto;
import com.reservation.hospitalReviews.domain.QHospitalReviewEntity;
import com.reservation.payment.domain.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.reservation.HospitalReservation.domain.QHospitalReservationEntity.hospitalReservationEntity;
import static com.reservation.hospitalReviews.domain.QHospitalReviewEntity.hospitalReviewEntity;
import static com.reservation.hospitals.domain.QHospitalEntity.hospitalEntity;
import static com.reservation.payment.domain.QPaymentEntity.paymentEntity;
import static com.reservation.user.domain.QSocialUserEntity.socialUserEntity;

@Repository
@RequiredArgsConstructor
public class HospitalReservationRepositoryImpl implements HospitalReservationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    //예약을 하기 위해서 병원 예약한 날짜, 시간 조회로 예약 정보를 확인한다.
    @Override
    public List<FindHospitalReservationDto> findAllByHospitalIdAndReservationDate(Long hospId, LocalDate reservationDate) {
        return jpaQueryFactory
            .select(Projections.constructor(FindHospitalReservationDto.class,
                hospitalReservationEntity.reservationAt, hospitalReservationEntity.reservationTime))
            .where(hospitalReservationEntity.hospital.hospId.eq(hospId),
                hospitalReservationEntity.reservationAt.eq(reservationDate)
                ,hospitalReservationEntity.reservationStatus.in(ReservationStatus.PENDING, ReservationStatus.SUCCESS, ReservationStatus.END)
            )
            .from(hospitalReservationEntity)
            .fetch();
    }

    //결제가 된 건지 체크 예약 날짜, 예약 시간으로 예약이 되었는지 확인
    @Override
    public Optional<FindHospitalReservationDto> findByHospIdAndReservationDateAndReservationTime(ReservationSaveRequest request) {
       return jpaQueryFactory
           .select(Projections.constructor(FindHospitalReservationDto.class,
                hospitalReservationEntity.hospital.hospId, hospitalReservationEntity.reservationAt, hospitalReservationEntity.reservationTime))
            .from(hospitalReservationEntity)
            .leftJoin(hospitalReservationEntity.hospital, hospitalEntity)
            .where(hospitalReservationEntity.hospital.hospId.eq(request.getHospId()),
                hospitalReservationEntity.reservationAt.eq(request.getReservationAt()),
                hospitalReservationEntity.reservationTime.eq(request.getReservationTime()),
                hospitalReservationEntity.reservationStatus.in(ReservationStatus.PENDING, ReservationStatus.SUCCESS)
            )
            .fetch().stream().findFirst();
    }

    //결제체크를 위한 예약아이디와 결제 상태 SUCCESS인 하나의 결제 내역 조회,
    @Override
    public FindOneHospitalReservationPaymentDto findOneHospitalReservation(Long hospitalReservationId) {
        return jpaQueryFactory
            .select(Projections.constructor(FindOneHospitalReservationPaymentDto.class,
                hospitalReservationEntity.payment.paymentId,hospitalReservationEntity.payment.importUid, hospitalReservationEntity.payment.amount
                )).from(hospitalReservationEntity)
            .leftJoin(hospitalReservationEntity.payment, paymentEntity)
            .where(hospitalReservationEntity.hospReservationId.eq(hospitalReservationId),
                hospitalReservationEntity.reservationStatus.in(ReservationStatus.PENDING),
                hospitalReservationEntity.payment.paymentStatus.eq(PaymentStatus.SUCCESS)).fetchOne();
    }

    // 유저가 예약한 예약정보 (전체, PENDING(예약만 성공),SUCCESS(리뷰 작성가능), END(리뷰 작성끝), CANCEL(변경?, 취소))
    // 예약이 성공 리뷰작성가능,리뷰작성완료,
    // 결제 성공, 결제 환불불가, 환불성공,
    //
    @Override
    public List<FindHospitalReservationDto> findHospitalReservationByUserId(Long userId) {
        return jpaQueryFactory
            .select(Projections.constructor(FindHospitalReservationDto.class,
                hospitalReservationEntity.hospReservationId,
                hospitalReservationEntity.reservationAt,
                hospitalReservationEntity.reservationTime,
                hospitalReservationEntity.user.socialUserId,
                hospitalReservationEntity.hospital.hospId,
                hospitalReservationEntity.hospital.yadmNm,
                hospitalReservationEntity.hospital.addr,
                hospitalReservationEntity.hospital.telno
                ))
            .from(hospitalReservationEntity)
            .leftJoin(hospitalReservationEntity.user, socialUserEntity).fetchJoin()
            .leftJoin(hospitalReservationEntity.hospital, hospitalEntity).fetchJoin()
            .where(hospitalReservationEntity.user.socialUserId.eq(userId))
            .fetch();
    }

    // 배치에서 사용 reservationAt으로 예약정보와 결제정보 조회
    @Override
    public List<HospitalReservationEntity> findAllByReservationAt(LocalDate reservationAt) {
        return jpaQueryFactory
            .selectFrom(hospitalReservationEntity)
            .leftJoin(hospitalReservationEntity.payment, paymentEntity).fetchJoin()
            .where(hospitalReservationEntity.reservationAt.eq(reservationAt))
            .fetch();
    }

    // 조회 예약이 PENDDING SUCCESS 보여준다.
    // 지난예약
    // 취소는 CANCEL
    // 리뷰작성까지 끝나면 END
    @Override
    public PageImpl<FindHospitalReservationDto> findAllByUserId(Long userId, String select, Pageable pageable){
        QueryResults<Long> reservaionIds = jpaQueryFactory
            .select(hospitalReservationEntity.hospReservationId)
            .from(hospitalReservationEntity)
            .where(
                hospitalReservationEntity.user.socialUserId.eq(userId),
                reservationType(select))
            .orderBy(hospitalReservationEntity.hospReservationId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<FindHospitalReservationDto> myReservationList = jpaQueryFactory
            .select(
                Projections.constructor(FindHospitalReservationDto.class,
                    hospitalReservationEntity.hospReservationId,
                    hospitalReservationEntity.reservationAt,
                    hospitalReservationEntity.reservationTime,
                    hospitalReservationEntity.reservationStatus,
                    hospitalReservationEntity.user.socialUserId.as("socialUserId"),
                    hospitalReservationEntity.hospital.hospId,
                    hospitalReservationEntity.hospital.yadmNm,
                    hospitalReservationEntity.hospital.addr,
                    hospitalReservationEntity.hospital.telno,
                    hospitalReviewEntity.hospReviewId
                )
            )
            .from(hospitalReservationEntity)
            .leftJoin(hospitalReservationEntity.hospital, hospitalEntity)
            .leftJoin(hospitalReservationEntity.user, socialUserEntity)
            .leftJoin(hospitalReservationEntity.hospitalReview, hospitalReviewEntity)
            .where(
                hospitalReservationEntity.hospReservationId.in(reservaionIds.getResults())
            )
            .orderBy(hospitalReservationEntity.hospReservationId.desc())
            .fetch();
        for(FindHospitalReservationDto aa : myReservationList){
            System.out.println("제발 리뷰 ==>>" + aa.getHospReviewId());
        }
     return new PageImpl<>(myReservationList, pageable, reservaionIds.getTotal());
    }

    // 예약 변경하기 위해서 하나의 예약 정보를 조회
    @Override
    public HospitalReservationEntity findOneByReservationId(Long reservationId) {
        return jpaQueryFactory
            .selectFrom(hospitalReservationEntity)
            .innerJoin(hospitalReservationEntity.payment, paymentEntity).fetchJoin()
            .where(hospitalReservationEntity.hospReservationId.eq(reservationId))
            .fetchOne();
    }


    public BooleanExpression reservationEq(String select){
            if(select.equals("1")) return hospitalReservationEntity.reservationStatus.in(ReservationStatus.PENDING,ReservationStatus.SUCCESS);
            else if(select.equals("2")) return hospitalReservationEntity.reservationStatus.eq(ReservationStatus.END);
             else if(select.equals("3")) return hospitalReservationEntity.reservationStatus.eq(ReservationStatus.CANCEL);
            else return null;
    }


    private BooleanExpression reservationType(String type) {
        return StringUtils.hasText(type) ? reservationEq(type): null;

    }

}
