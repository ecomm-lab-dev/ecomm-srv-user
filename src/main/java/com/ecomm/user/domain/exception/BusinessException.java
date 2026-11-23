package com.ecomm.user.domain.exception;

import io.grpc.Status;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final String code;
    private final Status grpcStatus;

    protected BusinessException(String code, String message, Status grpcStatus) {
        super(message);
        this.code = code;
        this.grpcStatus = grpcStatus;
    }
}
