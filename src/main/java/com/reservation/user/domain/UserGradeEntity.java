package com.reservation.user.domain;

import com.reservation.global.audit.MutableBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_grade")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserGradeEntity extends MutableBaseEntity {

    @Id
    @Column(name = "user_grade_id") private String userGradeId;

    @Column(name = "user_id") private Long userId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "grade_name") private GradeType gradeName;

    @Column(name = "valid_yn") private Boolean validYn;

    public UserGrade toDomain() {
        return UserGrade.builder()
                .userId(this.userId)
                .gradeType(this.gradeName)
                .validYn(this.validYn)
                .build();
    }

    public static UserGradeEntity toEntity(UserGrade userGrade) {
        return new UserGradeEntity(
                UUID.randomUUID().toString(),
                userGrade.getUserId(),
                userGrade.getGradeType(),
                userGrade.getValidYn()
        );
    }
}
