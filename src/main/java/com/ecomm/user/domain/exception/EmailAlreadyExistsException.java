package com.ecomm.user.domain.exception;

import io.grpc.Status;

public class EmailAlreadyExistsException extends BusinessException {

    private static final String CODE = "EMAIL_ALREADY_EXISTS";
    private static final String MESSAGE = "Email already registered";

    public EmailAlreadyExistsException() {
        super(CODE, MESSAGE, Status.ALREADY_EXISTS);
    }
}
