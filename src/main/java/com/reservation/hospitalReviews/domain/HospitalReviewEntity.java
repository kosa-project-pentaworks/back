package com.reservation.hospitalReviews.domain;

import com.reservation.HospitalReservation.domain.HospitalReservationEntity;
import com.reservation.global.audit.MutableBaseEntity;
import com.reservation.hospitalReviews.controller.requerst.HospitalReviewInputRequest;
import com.reservation.hospitalReviews.controller.requerst.HospitalReviewUpdateInputRequest;
import com.reservation.hospitals.domain.HospitalEntity;
import com.reservation.user.domain.SocialUserEntity;
import com.reservation.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "hospital_review")
@EntityListeners(AuditingEntityListener.class)
public class HospitalReviewEntity extends MutableBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hosp_review_id")
    private Long hospReviewId;

    @Column(name = "hosp_review_content",nullable = false)
    private String hospReviewContent;

    @Column(name = "hosp_review_rating", length = 2, nullable = false)
    private int hospReviewRating;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private SocialUserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "hosp_id")
    private HospitalEntity hospitalEntity;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_reservation_id")
    private HospitalReservationEntity hospitalReservation;

    @Builder
    public HospitalReviewEntity(Long hospReviewId, String hospReviewContent, int hospReviewRating, SocialUserEntity userEntity, HospitalEntity hospitalEntity, HospitalReservationEntity hospitalReservation) {
        this.hospReviewId = hospReviewId;
        this.hospReviewContent = hospReviewContent;
        this.hospReviewRating = hospReviewRating;
        this.userEntity = userEntity;
        this.hospitalEntity = hospitalEntity;
        this.hospitalReservation = hospitalReservation;
    }

    public static HospitalReviewEntity save(HospitalReviewInputRequest hospitalReviewInput, SocialUserEntity user, HospitalEntity hospital, HospitalReservationEntity hospitalReservation){
        return HospitalReviewEntity.builder()
            .hospReviewContent(hospitalReviewInput.getContent())
            .hospReviewRating(hospitalReviewInput.getRating())
            .userEntity(user)
            .hospitalEntity(hospital)
            .hospitalReservation(hospitalReservation)
            .build();
    }

    public HospitalReviewEntity update(HospitalReviewUpdateInputRequest hospitalReviewUpdateInput){
      this.hospReviewContent = hospitalReviewUpdateInput.getContent();
      this.hospReviewRating = hospitalReviewUpdateInput.getRating();
      return this;
    }
}
