package com.ecomm.user.application.usecase;

import com.ecomm.user.application.dto.CreateUserRequestDomainDTO;
import com.ecomm.user.domain.entity.User;
import com.ecomm.user.port.in.CreateUserUseCase;
import com.ecomm.user.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryPort userRepository;

    @Override
    public User createUser(CreateUserRequestDomainDTO request) {

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
        return userRepository.save(user);
    }
}
