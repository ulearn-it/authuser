package com.ncourses.authuser.user.service;

import com.ncourses.authuser.user.model.UserModel;
import com.ncourses.authuser.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<UserModel> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Transactional
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
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
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Transactional
    public UserModel saveUser(UserModel userModel) {
        userModel = save(userModel);
        return userModel;
    }

    @Transactional
    public void deleteUser(UserModel userModel) {
        delete(userModel);
    }

    @Transactional
    public UserModel updateUser(UserModel userModel) {
        userModel = save(userModel);
        return userModel;
    }

    @Transactional
    public UserModel updatePassword(UserModel userModel) {
        return save(userModel);
    }
}
