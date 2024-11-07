package com.reservation.user.repository;

import com.reservation.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>, UserCustomRepository {
}
