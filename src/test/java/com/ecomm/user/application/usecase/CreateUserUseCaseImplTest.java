package com.ecomm.user.application.usecase;

import com.ecomm.user.application.dto.CreateUserRequestDomainDTO;
import com.ecomm.user.domain.entity.User;
import com.ecomm.user.domain.enums.Role;
import com.ecomm.user.domain.exception.EmailAlreadyExistsException;
import com.ecomm.user.domain.exception.InvalidRequestException;
import com.ecomm.user.domain.valueobject.Address;
import com.ecomm.user.port.out.UserRepositoryPort;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    private static final String VALID_CPF = "52998224725";

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepositoryPort userRepository;

    private CreateUserUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        useCase = new CreateUserUseCaseImpl(validator, passwordEncoder, userRepository);
    }

    @Test
    void shouldCreateUserWithEncodedPasswordWhenRequestIsValid() {
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = useCase.createUser(validRequest());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertEquals(result, saved);
        assertEquals("encoded-password", saved.getPassword());
        assertNotEquals("plain-password", saved.getPassword());
        assertEquals("john@email.com", saved.getEmail());
        assertEquals(Role.CLIENT, saved.getRole());
        assertTrue(saved.isActive());
    }

    @Test
    void shouldRejectInvalidEmailAndNotPersist() {
        var request = new CreateUserRequestDomainDTO("John", "not-an-email", "plain-password",
                VALID_CPF, "11999999999", address(), LocalDate.of(1990, 1, 20));

        var ex = assertThrows(InvalidRequestException.class, () -> useCase.createUser(request));

        assertTrue(ex.getMessage().contains("email"));
        verifyNoInteractions(passwordEncoder, userRepository);
    }

    @Test
    void shouldRejectInvalidCpf() {
        var request = new CreateUserRequestDomainDTO("John", "john@email.com", "plain-password",
                "11111111111", "11999999999", address(), LocalDate.of(1990, 1, 20));

        var ex = assertThrows(InvalidRequestException.class, () -> useCase.createUser(request));

        assertTrue(ex.getMessage().contains("socialId: Invalid CPF"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldRejectFutureBirthDate() {
        var request = new CreateUserRequestDomainDTO("John", "john@email.com", "plain-password",
                VALID_CPF, "11999999999", address(), LocalDate.now().plusDays(1));

        var ex = assertThrows(InvalidRequestException.class, () -> useCase.createUser(request));

        assertTrue(ex.getMessage().contains("birthDate"));
    }

    @Test
    void shouldAggregateAllViolationsInSortedOrder() {
        var request = new CreateUserRequestDomainDTO("", "bad", "", VALID_CPF,
                "11999999999", null, LocalDate.of(1990, 1, 20));

        var ex = assertThrows(InvalidRequestException.class, () -> useCase.createUser(request));

        assertTrue(ex.getMessage().startsWith("address: Address is required; email: "));
        assertTrue(ex.getMessage().contains("; name: "));
        assertTrue(ex.getMessage().contains("; password: "));
    }

    @Test
    void shouldPropagateBusinessExceptionFromRepository() {
        when(passwordEncoder.encode(any())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenThrow(new EmailAlreadyExistsException());

        assertThrows(EmailAlreadyExistsException.class, () -> useCase.createUser(validRequest()));
    }

    private CreateUserRequestDomainDTO validRequest() {
        return new CreateUserRequestDomainDTO("John", "john@email.com", "plain-password",
                VALID_CPF, "11999999999", address(), LocalDate.of(1990, 1, 20));
    }

    private Address address() {
        return Address.of("Street", "City", 10, "SP", "01000-000", "BR");
    }
}
