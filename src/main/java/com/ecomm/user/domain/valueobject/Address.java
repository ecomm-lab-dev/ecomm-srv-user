package com.ecomm.user.domain.valueobject;

import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {

    private String street;
    private String city;
    private int number;
    private String state;
    private String zipCode;
    private String country;

    public static Address of(String street, String city, int number, String state, String zipCode, String country) {
        return Address.builder()
                .street(street)
                .city(city)
                .number(number)
                .state(state)
                .zipCode(zipCode)
                .country(country)
                .build();
    }
}
