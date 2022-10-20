package com.ncourses.authuser.role.repository;

import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.role.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findByRoleName(RoleType name);
}
