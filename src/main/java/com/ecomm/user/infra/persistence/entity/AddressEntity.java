package com.ecomm.user.infra.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Address")
@Table(name = "addresses")
public class AddressEntity {

    @Id
    private UUID id;
    private UUID userId;
    private String street;
    private String city;
    private int number;
    private String state;
    private String zipCode;
    private String country;
}
