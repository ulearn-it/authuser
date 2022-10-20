package com.ncourses.authuser.role.service;

import com.ncourses.authuser.role.model.RoleModel;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Optional<RoleModel> findByRoleName(RoleType roleType) {
        return roleRepository.findByRoleName(roleType);
    }
}
