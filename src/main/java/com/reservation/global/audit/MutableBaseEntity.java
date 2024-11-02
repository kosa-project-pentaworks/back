package com.reservation.global.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class MutableBaseEntity extends BaseEntity {

    @LastModifiedDate
    @Column(name = "modified_at") private LocalDateTime modifiedAt;

    @LastModifiedBy
    @Column(name = "modified_by") private String modifiedBy;
}
