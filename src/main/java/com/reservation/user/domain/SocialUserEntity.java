package com.reservation.user.domain;

import com.reservation.global.audit.MutableBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialUserEntity extends MutableBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_user_id") private Long socialUserId;

    @Column(name = "user_name") private String username;

    @Column(name = "provider") private String provider;

    @Column(name = "provider_id") private String providerId;

    @Column(name = "phone") private String phone;

    @Embedded
    @Column(name = "address") Address address;

    public SocialUserEntity(String username, String provider, String providerId) {
        this.username = username;
        this.provider = provider;
        this.providerId = providerId;
    }

    public SocialUserEntity updateContactDetails(String phone, Address address) {
        this.phone = phone;
        this.address = address;
        return this;
    }

    public UserDto toDomain() {
        return UserDto.builder()
                .userId(this.socialUserId)
                .username(this.username)
                .provider(this.provider)
                .providerId(this.providerId)
                .phone(this.phone)
                .build();
    }
}
