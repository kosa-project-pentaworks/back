package com.reservation.hospitalReviews.domain;

import com.reservation.hospitalReviews.controller.requerst.HospitalReviewInputRequest;
import com.reservation.hospitals.domain.HospitalEntity;
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
@EntityListeners(AuditingEntityListener.class)
public class HospitalReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hosp_review_id")
    private Long hospReviewId;
    @Column(name = "hosp_review_content",nullable = false)
    private String hospReviewContent;

    @Column(name = "hosp_review_rating", length = 2, nullable = false)
    private int hospReviewRating;

    @CreatedDate
    @Column(name = "created_at",updatable = false, columnDefinition = "datetime default now()")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at", columnDefinition = "datetime default now()")
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "hosp_id")
    private HospitalEntity hospitalEntity;

    public HospitalReviewEntity(Long hospReviewId, String hospReviewContent, int hospReviewRating, LocalDateTime createdAt, LocalDateTime modifiedAt, UserEntity userEntity, HospitalEntity hospitalEntity) {
        this.hospReviewId = hospReviewId;
        this.hospReviewContent = hospReviewContent;
        this.hospReviewRating = hospReviewRating;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.userEntity = userEntity;
        this.hospitalEntity = hospitalEntity;
    }

    @Builder
    public HospitalReviewEntity(HospitalReviewInputRequest hospitalReviewInputRequest, UserEntity userEntity, HospitalEntity hospitalEntity) {
        this.hospReviewContent = hospitalReviewInputRequest.content();
        this.hospReviewRating = hospitalReviewInputRequest.rating();
        this.userEntity = userEntity;
        this.hospitalEntity = hospitalEntity;
    }
}
