package com.reservation.user.domain;

import com.reservation.global.audit.MutableBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends MutableBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") private Long userId;

    @Column(name = "user_name") private String username;

    @Column(name = "provider") private String provider;

    @Column(name = "provider_id") private String providerId;

    @Column(name = "email") private String email;

    @Column(name = "phone") private String phone;

    public UserEntity(String username, String provider, String providerId) {
        this.username = username;
        this.provider = provider;
        this.providerId = providerId;
    }
}
