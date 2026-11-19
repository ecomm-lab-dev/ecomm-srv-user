package com.ecomm.user.domain.exception;

import io.grpc.Status;

public class InvalidRequestException extends BusinessException {

    private static final String CODE = "INVALID_REQUEST";

    public InvalidRequestException(String message) {
        super(CODE, message, Status.INVALID_ARGUMENT);
    }
}
