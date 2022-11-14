package com.ncourses.authuser.auth.service;

import com.ncourses.authuser.exception.UniqueFieldException;
import com.ncourses.authuser.role.model.RoleEntity;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.role.service.RoleService;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.model.enums.UserType;
import com.ncourses.authuser.user.service.UserService;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {

    PasswordEncoder passwordEncoder;
    RoleService roleService;
    UserService userService;

    public UserEntity registerUserStudent(UserDto userDto) {
        validateUser(userDto);
        RoleEntity role = roleService.findByRoleName(RoleType.ROLE_STUDENT);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userDto, user);
        user.setUserType(UserType.STUDENT);
        user.getRoles().add(role);
        userService.saveUser(user);
        return user;
    }

    public UserEntity registerUserAdmin(UserDto userDto) {
        validateUser(userDto);
        RoleEntity role = roleService.findByRoleName(RoleType.ROLE_ADMIN);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userDto, user);
        user.setUserType(UserType.ADMIN);
        user.getRoles().add(role);
        userService.saveUser(user);
        return user;
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
