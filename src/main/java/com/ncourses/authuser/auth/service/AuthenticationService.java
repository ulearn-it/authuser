package com.ncourses.authuser.auth.service;

import com.ncourses.authuser.exception.UniqueFieldException;
import com.ncourses.authuser.role.model.RoleEntity;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.role.service.RoleService;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.model.enums.UserType;
import com.ncourses.authuser.user.model.mapper.UserMapper;
import com.ncourses.authuser.user.service.UserService;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {

    PasswordEncoder passwordEncoder;
    RoleService roleService;
    UserService userService;
    UserMapper userMapper;

    @Transactional
    public UserEntity registerUserStudent(UserDto userDto) {
        validateUser(userDto);

        RoleEntity role = roleService.findByRoleName(RoleType.ROLE_STUDENT);

        UserEntity user = userMapper.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserType(UserType.STUDENT);
        user.getRoles().add(role);

        return userService.saveUser(user);
    }

    @Transactional
    public UserEntity registerUserAdmin(UserDto userDto) {
        validateUser(userDto);

        RoleEntity role = roleService.findByRoleName(RoleType.ROLE_ADMIN);

        UserEntity user = userMapper.userDtoToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserType(UserType.ADMIN);
        user.getRoles().add(role);

        return userService.saveUser(user);
    }

    private void validateUser(UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            throw new UniqueFieldException("Username already taken!");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            throw new UniqueFieldException("Email already taken!");
        }
    }

}
