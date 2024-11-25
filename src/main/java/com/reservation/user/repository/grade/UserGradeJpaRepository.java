package com.reservation.user.repository.grade;

import com.reservation.user.domain.UserGradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGradeJpaRepository extends JpaRepository<UserGradeEntity, String>, UserGradeCustomRepository {

    void deleteByUserId(Long userId);
}
