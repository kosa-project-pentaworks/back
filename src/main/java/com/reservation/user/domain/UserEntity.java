package com.reservation.user.domain;

import com.reservation.global.audit.MutableBaseEntity;
import com.reservation.user.repository.request.CreateUser;
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

    @Column(name = "password") private String password;

    @Column(name = "email") private String email;


    public UserEntity(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserDto toDomain() {
        return UserDto.builder()
                .userId(this.userId)
                .username(this.username)
                .encryptedPassword(this.password)
                .email(this.email)
                .build();
    }

    public static UserEntity toEntity(CreateUser createUser) {
        return new UserEntity(
                createUser.getUsername(),
                createUser.getEncryptedPassword(),
                createUser.getEmail()
        );
    }
}
