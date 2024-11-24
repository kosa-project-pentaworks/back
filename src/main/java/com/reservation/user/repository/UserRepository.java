package com.reservation.user.repository;

import com.reservation.global.exception.UserException;
import com.reservation.user.domain.*;
import com.reservation.user.repository.grade.UserGradeRepository;
import com.reservation.user.repository.port.DeleteUserPort;
import com.reservation.user.repository.port.FetchUserPort;
import com.reservation.user.repository.port.InsertUserPort;
import com.reservation.user.repository.port.UpdateUserPort;
import com.reservation.user.repository.request.CreateUser;
import com.reservation.user.repository.request.UpdateSocialUser;
import com.reservation.user.repository.social.SocialUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserRepository implements FetchUserPort, InsertUserPort, UpdateUserPort, DeleteUserPort {

    private final UserJpaRepository userJpaRepository;
    private final SocialUserJpaRepository socialUserJpaRepository;
    private final UserGradeRepository userGradeRepository;

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

        Optional<UserGrade> byUserId = userGradeRepository.findByUserId(socialUserEntity.getSocialUserId());

        return Optional.of(new UserDto(
                socialUserEntity.getSocialUserId(),
                socialUserEntity.getUsername(),
                null,
                null,
                socialUserEntity.getProvider(),
                socialUserEntity.getProviderId(),
                byUserId.orElse(UserGrade.newGrade(socialUserEntity.getSocialUserId(), GradeType.BASIC))
                        .getGradeType()
                        .toRole(),
                socialUserEntity.getPhone(),
                socialUserEntity.getAddress()
        ));
    }

    @Override
    public UserDto create(CreateUser create) {
        UserEntity userEntity = UserEntity.toEntity(create);
        UserEntity savedUserEntity = userJpaRepository.save(userEntity);

        userJpaRepository.flush();

        userGradeRepository.create(savedUserEntity.getUserId(), GradeType.ADMIN);

        return savedUserEntity.toDomain();
    }

    @Override
    public UserDto createSocialUser(String username, String provider, String providerId) {
        SocialUserEntity socialUserEntity = new SocialUserEntity(username, provider, providerId);
        SocialUserEntity savedSocialUserEntity = socialUserJpaRepository.save(socialUserEntity);

        socialUserJpaRepository.flush();

        userGradeRepository.create(savedSocialUserEntity.getSocialUserId(), GradeType.BASIC);

        return savedSocialUserEntity.toDomain();
    }

    @Override
    public UserDto updateSocialUser(UpdateSocialUser update) {
        SocialUserEntity socialUser = socialUserJpaRepository.findByProviderId(update.getProviderId())
                .orElseThrow(() -> new UserException.UserDoesNotExistException());

        socialUser.updateContactDetails(update.getPhone(), update.getAddress());
        socialUserJpaRepository.save(socialUser);

        return socialUser.toDomain();
    }

    @Override
    public void deleteByProviderId(String providerId) {
        socialUserJpaRepository.deleteByProviderId(providerId);
    }
}
