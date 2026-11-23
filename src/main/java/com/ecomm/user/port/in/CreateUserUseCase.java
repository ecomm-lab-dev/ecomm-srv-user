package com.ecomm.user.port.in;

import com.ecomm.user.application.dto.CreateUserRequestDomainDTO;
import com.ecomm.user.domain.entity.User;

public interface CreateUserUseCase {
    User createUser(CreateUserRequestDomainDTO request);
}
