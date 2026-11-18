package com.ecomm.user.infra.persistence.mapper;

import com.ecomm.user.domain.entity.User;
import com.ecomm.user.domain.valueobject.Address;
import com.ecomm.user.infra.persistence.entity.AddressEntity;
import com.ecomm.user.infra.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    public UserEntity toJpa(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .socialId(user.getSocialId())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .isActive(user.isActive())
                .role(user.getRole())
                .build();
    }

    public AddressEntity toJpa(Address address, UUID userId) {
        return AddressEntity.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .street(address.getStreet())
                .city(address.getCity())
                .number(address.getNumber())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .build();
    }
}
