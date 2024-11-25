package com.reservation.payment.service;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.HospitalReservation.domain.ReservationStatus;
import com.reservation.HospitalReservation.repository.HospitalReservationRepository;
import com.reservation.HospitalReservation.repository.custom.dto.FindOneHospitalReservationPaymentDto;
import com.reservation.HospitalReservation.service.HospitalReservationService;
import com.reservation.global.exception.HospitalException;
import com.reservation.global.exception.HospitalReviewException;
import com.reservation.global.exception.UserException;
import com.reservation.global.redis.client.ApplicationLockClient;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.hospitals.repository.HospitalRepository;
import com.reservation.payment.controller.PaymentClient;
import com.reservation.payment.controller.request.RefundPaymentRequerst;
import com.reservation.payment.controller.response.CheckPaymentResponse;
import com.reservation.payment.controller.request.CreatePaymentRequest;
import com.reservation.payment.controller.request.PaymentTokentRequest;
import com.reservation.payment.controller.response.PaymentRefundResponse;
import com.reservation.payment.controller.response.PaymentTokenResponse;
import com.reservation.payment.domain.PaymentEntity;
import com.reservation.payment.domain.PaymentStatus;
import com.reservation.payment.repository.PaymentRepository;
import com.reservation.user.domain.SocialUserEntity;
import com.reservation.user.repository.social.SocialUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final HospitalRepository hospitalRepository;
    private final SocialUserJpaRepository socialUserJpaRepository;
    private final HospitalReservationService hospitalReservationService;
    private final ApplicationLockClient applicationLockClient;
    private final PaymentClient paymentClient;
    private final HospitalReservationRepository hospitalReservationRepository;

    @Value("${import.restkey}")
    private String impKey;
    @Value("${import.secretkey}")
    private String impSecret;

    @Transactional
    public Boolean createPayment(CreatePaymentRequest createPaymentRequest){
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                releaseLock(createPaymentRequest.getRedisKey());
            }
        });
        HospitalEntity isValidHospital = hospitalRepository.findById(createPaymentRequest.getHospId())
            .orElseThrow(HospitalException.HospitalDoesNotExitException::new);
        SocialUserEntity isValidUser = socialUserJpaRepository.findByProviderId(createPaymentRequest.getProviderId())
            .orElseThrow(UserException.UserDoesNotExistException::new);
        // 결제 유저, 병원 있는지 체크

            PaymentEntity payment = PaymentEntity.from(createPaymentRequest, isValidHospital, isValidUser);
            PaymentEntity createdPayment = paymentRepository.save(payment);
            // 결제정보를 저장되었는지
            HospitalReservationEntity hospitalReservation = HospitalReservationEntity.from(createPaymentRequest, isValidUser, isValidHospital, createdPayment);
            Boolean result = hospitalReservationService.createReservation(hospitalReservation);
        if(!result) throw new HospitalReviewException.HospitalReviewDoesNotExit();
        return true;
    }
    public void releaseLock(String releaseLockKey){
        applicationLockClient.remove(releaseLockKey);
    }

    // 환불
    @Transactional
    public Boolean refund(Long hospitalReservationId){
        String token = getToken();
        HospitalReservationEntity findOneHospitalReservation = hospitalReservationRepository.findOneByReservationId(hospitalReservationId);
        PaymentEntity findPayment = findOneHospitalReservation.getPayment();
        String refundImpUid = checkPaymentByReservationId(findOneHospitalReservation.getHospReservationId(), token);
        RefundPaymentRequerst refund = new RefundPaymentRequerst(refundImpUid);
        PaymentRefundResponse result = paymentClient.refund(refund,token);
        if(result.getCode() == 0) {
            // 성공시 병원 예약은 cancel로 바꾸고 결제는 refund로 바꿔야한다.
            findOneHospitalReservation.updateStatus(ReservationStatus.CANCEL);
            findPayment.updateStatus(PaymentStatus.REFUND);
            return true;
        }
        else return false;

    }

    public String getToken(){
        PaymentTokenResponse result = paymentClient.getToken(new PaymentTokentRequest(impKey, impSecret));
        return result.getResponse().getAccess_token();
    }

    public String checkPaymentByReservationId(Long reservationId,String token){
        FindOneHospitalReservationPaymentDto result = hospitalReservationService.findOneHospitalReservation(reservationId);
        CheckPaymentResponse detailPayment = paymentClient.checkPayment(result.getImpUid(), token);
        if(!detailPayment.getCode().equals("0")) throw new RuntimeException();
        return result.getImpUid();
    }

}
