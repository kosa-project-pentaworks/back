package com.reservation.token.domain;

import com.reservation.global.audit.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenEntity extends MutableBaseEntity {

    @Id
    @Column(name = "token_id") private String tokenId;

    @Column(name = "user_id") private String userId;

    @Column(name = "access_token") private String accessToken;

    @Column(name = "refresh_token") private String refreshToken;

    @Column(name = "access_token_expires_at") private LocalDateTime accessTokenExpiresAt;

    @Column(name = "refresh_token_expires_at") private LocalDateTime refreshTokenExpiresAt;

    public TokenEntity(String userId, String accessToken, String refreshToken, LocalDateTime accessTokenExpiresAt, LocalDateTime refreshTokenExpiresAt) {
        this.tokenId = UUID.randomUUID().toString();
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public void updateToken(String accessToken, String refreshToken) {
        LocalDateTime now = LocalDateTime.now();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = getAccessTokenExpiredAt(now);
        this.refreshTokenExpiresAt = getRefreshTokenExpiredAt(now);
    }

    public TokenDto toDomain() {
        return TokenDto.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .accessTokenExpireAt(this.accessTokenExpiresAt)
                .refreshTokenExpireAt(this.refreshTokenExpiresAt)
                .build();
    }

    public static TokenEntity toEntity(
            String userId,
            String accessToken,
            String refreshToken
    ) {
        LocalDateTime now = LocalDateTime.now();

        return new TokenEntity(
                userId,
                accessToken,
                refreshToken,
                getAccessTokenExpiredAt(now),
                getRefreshTokenExpiredAt(now)
        );
    }

    private static LocalDateTime getAccessTokenExpiredAt(LocalDateTime now) {
        return now.plusHours(3);
    }

    private static LocalDateTime getRefreshTokenExpiredAt(LocalDateTime now) {
        return now.plusHours(24);
    }
}
