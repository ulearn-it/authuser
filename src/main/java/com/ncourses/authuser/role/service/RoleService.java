package com.ncourses.authuser.role.service;

import com.ncourses.authuser.exception.ResourceNotFoundException;
import com.ncourses.authuser.role.model.RoleEntity;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.role.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleService {

    RoleRepository roleRepository;

    public RoleEntity findByRoleName(RoleType roleType) {
        return roleRepository.findByRoleName(roleType)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleType.name()));
    }
}
