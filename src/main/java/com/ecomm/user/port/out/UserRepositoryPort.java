package com.ecomm.user.port.out;

import com.ecomm.user.domain.entity.User;

public interface UserRepositoryPort {
    User save(User user);
}
