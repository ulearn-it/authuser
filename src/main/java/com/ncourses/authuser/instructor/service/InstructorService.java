package com.ncourses.authuser.instructor.service;

import com.ncourses.authuser.instructor.model.dto.InstructorDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InstructorService {

    RoleService roleService;
    UserService userService;
    UserMapper userMapper;

    @Transactional
    public UserDto saveSubscriptionInstructor(InstructorDto instructorDto) {
        UserEntity user = userService.findById(instructorDto.getUserId());
        RoleEntity roleModel = roleService.findByRoleName(RoleType.ROLE_INSTRUCTOR);

        user.setUserType(UserType.INSTRUCTOR);
        user.getRoles().add(roleModel);

        return userMapper.userToUserDto(user);
    }
}
