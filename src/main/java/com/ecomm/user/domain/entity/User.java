package com.ecomm.user.domain.entity;

import com.ecomm.user.domain.enums.Role;
import com.ecomm.user.domain.valueobject.Address;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private String socialId;
    private String phoneNumber;
    private Address address;
    private LocalDate birthDate;

    private boolean isActive;

    private Role role;

    public static User createUser(String name, String email, String password, String socialId, String phoneNumber, Address address, LocalDate birthDate) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(name)
                .email(email)
                .password(password)
                .socialId(socialId)
                .phoneNumber(phoneNumber)
                .address(address)
                .birthDate(birthDate)
                .isActive(true)
                .role(Role.CLIENT)
                .build();
    }
}
