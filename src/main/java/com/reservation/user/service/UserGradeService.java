package com.reservation.user.service;

import com.reservation.user.domain.GradeType;
import com.reservation.user.domain.UserGrade;
import com.reservation.user.repository.port.DeleteUserGradePort;
import com.reservation.user.repository.port.FetchUserGradePort;
import com.reservation.user.repository.port.UpdateUserGradePort;
import com.reservation.user.service.usecase.GradeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGradeService implements GradeUseCase {

    private final FetchUserGradePort fetchUserGradePort;
    private final UpdateUserGradePort updateUserGradePort;
    private final DeleteUserGradePort deleteUserGradePort;

    public String findUserRoleByUserId(Long userId) {
        return fetchUserGradePort.findByUserId(userId)
                .map(UserGrade::getGradeType)
                .map(GradeType::getDesc)
                .orElse("권한 정보가 없습니다.");
    }

    @Override
    public void changeGrade(Long userId, String type) {
        GradeType gradeType = GradeType.valueOf(type);
        UserGrade userGrade = fetchUserGradePort.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 권한이 없습니다."));

        deleteUserGradePort.deleteByUserId(userId);

        userGrade.change(gradeType);
        updateUserGradePort.update(userGrade);
    }
}
