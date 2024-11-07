package com.reservation.token.repository;

import com.reservation.token.domain.TokenDto;
import com.reservation.token.domain.TokenEntity;
import com.reservation.token.repository.port.FetchTokenPort;
import com.reservation.token.repository.port.InsertTokenPort;
import com.reservation.token.repository.port.UpdateTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class TokenRepository implements FetchTokenPort, InsertTokenPort, UpdateTokenPort {

    private final TokenJpaRepository tokenJpaRepository;

    @Override
    public TokenDto create(String userId, String accessToken, String refreshToken) {
        TokenEntity entity = TokenEntity.toEntity(userId, accessToken, refreshToken);
        return tokenJpaRepository.save(entity)
                .toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TokenDto> findByUserId(String userId) {
        return tokenJpaRepository.findByUserId(userId)
                .map(TokenEntity::toDomain);
    }

    @Override
    public void updateToken(String userId, String accessToken, String refreshToken) {
        Optional<TokenEntity> byUserId = tokenJpaRepository.findByUserId(userId);

        if (byUserId.isEmpty()) {
            throw new RuntimeException();
        }

        TokenEntity tokenEntity = byUserId.get();
        tokenEntity.updateToken(accessToken, refreshToken);
        tokenJpaRepository.save(tokenEntity);
    }
}
