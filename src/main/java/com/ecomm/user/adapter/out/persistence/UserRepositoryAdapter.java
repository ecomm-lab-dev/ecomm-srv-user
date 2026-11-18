package com.ecomm.user.adapter.out.persistence;

import com.ecomm.user.domain.entity.User;
import com.ecomm.user.domain.exception.EmailAlreadyExistsException;
import com.ecomm.user.domain.exception.SocialIdAlreadyExistsException;
import com.ecomm.user.infra.persistence.mapper.UserMapper;
import com.ecomm.user.infra.persistence.repository.JpaAddressRepository;
import com.ecomm.user.infra.persistence.repository.JpaUserRepository;
import com.ecomm.user.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final JpaAddressRepository jpaAddressRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User save(User user) {
        try {
            jpaUserRepository.saveAndFlush(userMapper.toJpa(user));
            jpaAddressRepository.saveAndFlush(userMapper.toJpa(user.getAddress(), user.getId()));
            return user;
        } catch (DataIntegrityViolationException exception) {
            String message = exception.getMessage() == null ? "" : exception.getMessage().toLowerCase();
            if (message.contains("uk_users_email")) {
                throw new EmailAlreadyExistsException();
            }
            if (message.contains("uk_users_social_id")) {
                throw new SocialIdAlreadyExistsException();
            }
            throw exception;
        }
    }
}
