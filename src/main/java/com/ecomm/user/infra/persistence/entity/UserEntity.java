package com.ecomm.user.infra.persistence.entity;

import com.ecomm.user.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "User")
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String socialId;
    private String phoneNumber;
    private LocalDate birthDate;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private Role role;
}
