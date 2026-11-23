package com.ecomm.user.application.usecase;

import com.ecomm.user.application.dto.CreateUserRequestDomainDTO;
import com.ecomm.user.domain.entity.User;
import com.ecomm.user.domain.exception.InvalidRequestException;
import com.ecomm.user.port.in.CreateUserUseCase;
import com.ecomm.user.port.out.UserRepositoryPort;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final Validator validator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryPort userRepository;

    @Override
    public User createUser(CreateUserRequestDomainDTO request) {

        log.info("Creating user");
        validate(request);
        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.createUser(
                request.name(),
                request.email(),
                encodedPassword,
                request.socialId(),
                request.phoneNumber(),
                request.address(),
                request.birthDate()
        );
        User savedUser = userRepository.save(user);
        log.info("User created. userId={}", savedUser.getId());
        return savedUser;
    }

    private void validate(CreateUserRequestDomainDTO command) {
        var message = validator.validate(command).stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .sorted()
                .collect(Collectors.joining("; "));
        if (!message.isEmpty()) {
            log.warn("User creation rejected: validation failed. violations={}", message);
            throw new InvalidRequestException(message);
        }
    }
}
