package com.ecomm.user.infra.persistence.repository;

import com.ecomm.user.infra.persistence.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAddressRepository extends JpaRepository<AddressEntity, UUID> {
}
