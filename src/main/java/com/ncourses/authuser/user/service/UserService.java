package com.ncourses.authuser.user.service;

import com.ncourses.authuser.exception.MismatchedPasswordException;
import com.ncourses.authuser.exception.ResourceNotFoundException;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserEntity findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
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
    public Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    public UserEntity updateUser(UUID userId, UserDto userDto) {
        UserEntity user = findById(userId);
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setCpf(userDto.getCpf());
        return user;
    }

    @Transactional
    public UserEntity updatePassword(UUID userId, UserDto userDto) {
        UserEntity user = findById(userId);
        if (!user.getPassword().equals(userDto.getOldPassword())) {
            throw new MismatchedPasswordException("Mismatched old password!");
        }
        user.setPassword(userDto.getPassword());
        return user;
    }

    @Transactional
    public UserEntity updateImage(UUID userId, UserDto userDto) {
        UserEntity user = findById(userId);
        user.setImageUrl(userDto.getImageUrl());
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return user;
    }

    @Transactional
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }
}
