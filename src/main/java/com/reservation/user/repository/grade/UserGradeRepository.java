package com.reservation.user.repository.grade;

import com.reservation.user.domain.GradeType;
import com.reservation.user.domain.UserGrade;
import com.reservation.user.domain.UserGradeEntity;
import com.reservation.user.repository.port.DeleteUserGradePort;
import com.reservation.user.repository.port.FetchUserGradePort;
import com.reservation.user.repository.port.InsertUserGradePort;
import com.reservation.user.repository.port.UpdateUserGradePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserGradeRepository implements FetchUserGradePort, InsertUserGradePort, UpdateUserGradePort, DeleteUserGradePort {

    private final UserGradeJpaRepository userGradeJpaRepository;

    @Override
    public Optional<UserGrade> findByUserId(Long userId) {
        return userGradeJpaRepository.findByUserId(userId);
    }

    @Override
    public UserGrade create(Long userId, GradeType gradeType) {
        UserGrade userGrade = UserGrade.newGrade(userId, gradeType);
        UserGradeEntity entity = UserGradeEntity.toEntity(userGrade);

        return userGradeJpaRepository.save(entity)
                .toDomain();
    }

    @Override
    public UserGrade update(UserGrade userGrade) {
        return userGradeJpaRepository.save(UserGradeEntity.toEntity(userGrade))
                .toDomain();
    }

    @Override
    public void deleteByUserId(Long userId) {
        userGradeJpaRepository.deleteByUserId(userId);
    }
}
