package com.reservation.user.repository;

import com.reservation.global.exception.UserException;
import com.reservation.user.domain.SocialUserEntity;
import com.reservation.user.domain.UserDto;
import com.reservation.user.domain.UserEntity;
import com.reservation.user.repository.port.FetchUserPort;
import com.reservation.user.repository.port.InsertUserPort;
import com.reservation.user.repository.request.CreateUser;
import com.reservation.user.repository.social.SocialUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserRepository implements FetchUserPort, InsertUserPort {

    private final UserJpaRepository userJpaRepository;
    private final SocialUserJpaRepository socialUserJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(UserEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getByEmail(String email) {
        Optional<UserDto> byEmail = findByEmail(email);

        if (byEmail.isEmpty()) {
            throw new UserException.UserDoesNotExistException();
        }

        return byEmail.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByProviderId(String providerId) {
        Optional<SocialUserEntity> userEntityOptional = socialUserJpaRepository.findByProviderId(providerId);
        if (userEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        SocialUserEntity socialUserEntity = userEntityOptional.get();

        return Optional.of(new UserDto(
                socialUserEntity.getSocialUserId(),
                socialUserEntity.getUsername(),
                null,
                null,
                socialUserEntity.getProvider(),
                socialUserEntity.getProviderId(),
                "null"
        ));
    }

    @Override
    public UserDto create(CreateUser create) {
        UserEntity user = UserEntity.toEntity(create);
        return userJpaRepository.save(user)
                .toDomain();
    }

    @Override
    @Transactional
    public UserDto createSocialUser(String username, String provider, String providerId) {
        SocialUserEntity socialUserEntity = new SocialUserEntity(username, provider, providerId);
        return socialUserJpaRepository.save(socialUserEntity)
                .toDomain();
    }
}
