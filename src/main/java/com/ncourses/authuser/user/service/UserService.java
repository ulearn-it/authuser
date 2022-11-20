package com.ncourses.authuser.user.service;

import com.ncourses.authuser.exception.MismatchedPasswordException;
import com.ncourses.authuser.exception.ResourceNotFoundException;
import com.ncourses.authuser.user.constants.UserConstants;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.model.dtos.UserListDto;
import com.ncourses.authuser.user.model.mapper.UserMapper;
import com.ncourses.authuser.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserEntity findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(UserConstants.USER_NOT_FOUND_WITH_ID, userId)));
    }

    public void deleteById(UUID userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(MessageFormat.format(UserConstants.USER_NOT_FOUND_WITH_ID, userId));
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<UserListDto> findAll(Specification<UserEntity> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable)
                .map(user -> userMapper.userToUserListDto(user));
    }

    @Transactional
    public UserDto updateUser(UUID userId, UserDto userDto) {
        UserEntity user = findById(userId);
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return userMapper.userToUserDto(user);
    }

    @Transactional
    public UserDto updatePassword(UUID userId, UserDto userDto) {
        UserEntity user = findById(userId);
        if (!user.getPassword().equals(userDto.getOldPassword())) {
            throw new MismatchedPasswordException("Mismatched old password!");
        }
        user.setPassword(userDto.getPassword());
        return userMapper.userToUserDto(user);
    }

    @Transactional
    public UserDto updateImage(UUID userId, UserDto userDto) {
        UserEntity user = findById(userId);
        user.setImageUrl(userDto.getImageUrl());
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return userMapper.userToUserDto(user);
    }

    @Transactional
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }
}
