package com.ecomm.user.adapter.in.grpc;

import com.ecomm.user.application.dto.CreateUserRequestDomainDTO;
import com.ecomm.user.domain.entity.User;
import com.ecomm.user.domain.exception.EmailAlreadyExistsException;
import com.ecomm.user.domain.exception.InvalidRequestException;
import com.ecomm.user.domain.valueobject.Address;
import com.ecomm.user.port.in.CreateUserUseCase;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.CreateUserRequest;
import user.CreateUserResponse;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserAdapterTest {

    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private StreamObserver<CreateUserResponse> observer;

    @InjectMocks
    private CreateUserAdapter adapter;

    @Test
    void shouldMapRequestAndReturnResponseOnSuccess() {
        User user = User.createUser("John", "john@email.com", "encoded", "52998224725",
                "11999999999", Address.of("Street", "City", 10, "SP", "01000-000", "BR"),
                LocalDate.of(1990, 1, 20));
        when(createUserUseCase.createUser(any())).thenReturn(user);

        adapter.createUser(request("1990-01-20"), observer);

        ArgumentCaptor<CreateUserRequestDomainDTO> captor = ArgumentCaptor.forClass(CreateUserRequestDomainDTO.class);
        verify(createUserUseCase).createUser(captor.capture());
        assertEquals(LocalDate.of(1990, 1, 20), captor.getValue().birthDate());
        assertEquals("John", captor.getValue().name());
        assertEquals("Street", captor.getValue().address().getStreet());

        ArgumentCaptor<CreateUserResponse> response = ArgumentCaptor.forClass(CreateUserResponse.class);
        verify(observer).onNext(response.capture());
        verify(observer).onCompleted();
        assertEquals(user.getId().toString(), response.getValue().getId());
        assertEquals("john@email.com", response.getValue().getEmail());
    }

    @Test
    void shouldReturnInvalidArgumentWhenBirthDateFormatIsInvalid() {
        adapter.createUser(request("20/01/1990"), observer);

        StatusRuntimeException error = captureError();
        assertEquals(Status.Code.INVALID_ARGUMENT, error.getStatus().getCode());
        assertEquals("birthDate: must be in the format yyyy-MM-dd", error.getStatus().getDescription());
        verifyNoInteractions(createUserUseCase);
        verify(observer, never()).onCompleted();
    }

    @Test
    void shouldReturnInvalidArgumentWhenUseCaseRejectsValidation() {
        when(createUserUseCase.createUser(any())).thenThrow(new InvalidRequestException("email: invalid"));

        adapter.createUser(request("1990-01-20"), observer);

        StatusRuntimeException error = captureError();
        assertEquals(Status.Code.INVALID_ARGUMENT, error.getStatus().getCode());
        assertEquals("email: invalid", error.getStatus().getDescription());
    }

    @Test
    void shouldReturnAlreadyExistsWhenEmailIsDuplicated() {
        when(createUserUseCase.createUser(any())).thenThrow(new EmailAlreadyExistsException());

        adapter.createUser(request("1990-01-20"), observer);

        StatusRuntimeException error = captureError();
        assertEquals(Status.Code.ALREADY_EXISTS, error.getStatus().getCode());
        assertEquals("Email already registered", error.getStatus().getDescription());
    }

    private StatusRuntimeException captureError() {
        ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
        verify(observer).onError(captor.capture());
        return (StatusRuntimeException) captor.getValue();
    }

    private CreateUserRequest request(String birthDate) {
        return CreateUserRequest.newBuilder()
                .setName("John")
                .setEmail("john@email.com")
                .setPassword("plain-password")
                .setSocialId("52998224725")
                .setPhoneNumber("11999999999")
                .setBirthDate(birthDate)
                .setAddress(user.Address.newBuilder()
                        .setStreet("Street").setCity("City").setNumber(10)
                        .setState("SP").setZipCode("01000-000").setCountry("BR"))
                .build();
    }
}
