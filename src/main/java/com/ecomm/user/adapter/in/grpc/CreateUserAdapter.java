package com.ecomm.user.adapter.in.grpc;

import com.ecomm.user.application.dto.CreateUserRequestDomainDTO;
import com.ecomm.user.domain.exception.BusinessException;
import com.ecomm.user.domain.exception.InvalidRequestException;
import com.ecomm.user.domain.valueobject.Address;
import com.ecomm.user.port.in.CreateUserUseCase;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import user.CreateUserGrpc;
import user.CreateUserResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CreateUserAdapter extends CreateUserGrpc.CreateUserImplBase {

    private static final DateTimeFormatter BIRTH_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final CreateUserUseCase createUserUseCase;

    @Override
    public void createUser(user.CreateUserRequest req, StreamObserver<CreateUserResponse> obs) {

        try {
            log.info("Received createUser request: {}", req);
            CreateUserRequestDomainDTO command = toCommand(req);

            var user = createUserUseCase.createUser(command);

            obs.onNext(CreateUserResponse.newBuilder()
                    .setId(user.getId().toString())
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build());
            obs.onCompleted();
        } catch (BusinessException exception) {
            log.warn("Business error creating user. code={}, message={}", exception.getCode(), exception.getMessage());
            obs.onError(exception.getGrpcStatus()
                    .withDescription(exception.getMessage())
                    .asRuntimeException());
        }
    }

    private CreateUserRequestDomainDTO toCommand(user.CreateUserRequest req) {
        return new CreateUserRequestDomainDTO(
                req.getName(),
                req.getEmail(),
                req.getPassword(),
                req.getSocialId(),
                req.getPhoneNumber(),
                Address.of(
                        req.getAddress().getStreet(),
                        req.getAddress().getCity(),
                        req.getAddress().getNumber(),
                        req.getAddress().getState(),
                        req.getAddress().getZipCode(),
                        req.getAddress().getCountry()
                ),
                parseBirthDate(req.getBirthDate())
        );
    }

    private LocalDate parseBirthDate(String birthDate) {
        try {
            return LocalDate.parse(birthDate, BIRTH_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidRequestException("birthDate: must be in the format yyyy-MM-dd");
        }
    }
}
