package com.ecomm.user.application.dto;

import com.ecomm.user.domain.valueobject.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record CreateUserRequestDomainDTO(
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String password,
        @CPF(message = "Invalid CPF")
        String socialId,
        @NotBlank
        String phoneNumber,
        @NotNull(message = "Address is required")
        Address address,
        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate) {

}
