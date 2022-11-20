package com.ncourses.authuser.user.model.mapper;

import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.model.dtos.UserListDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(UserEntity user);

    UserEntity userDtoToUser(UserDto userDto);

    UserListDto userToUserListDto(UserEntity userEntity);

}
