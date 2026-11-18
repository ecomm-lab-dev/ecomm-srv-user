package com.ecomm.user.domain.exception;

import io.grpc.Status;

public class SocialIdAlreadyExistsException extends BusinessException {

    private static final String CODE = "SOCIAL_ID_ALREADY_EXISTS";
    private static final String MESSAGE = "Social ID already registered";

    public SocialIdAlreadyExistsException() {
        super(CODE, MESSAGE, Status.ALREADY_EXISTS);
    }
}
