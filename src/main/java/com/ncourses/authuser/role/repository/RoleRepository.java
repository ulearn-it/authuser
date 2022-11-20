package com.ncourses.authuser.role.repository;

import com.ncourses.authuser.role.model.RoleEntity;
import com.ncourses.authuser.role.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByRoleName(RoleType name);
}
