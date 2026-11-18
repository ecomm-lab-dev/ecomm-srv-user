package com.ecomm.user.application.dto;

import com.ecomm.user.domain.valueobject.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
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
        @NotBlank
        Address address,
        @NotBlank(message = "Birth date is required")
        @Max(value = 10, message = "Birth date must be in the format yyyy-MM-dd")
        LocalDate birthDate) {

}
