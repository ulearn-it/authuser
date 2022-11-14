package com.ncourses.authuser.user.service;

import com.ncourses.authuser.user.model.UserDetailsImpl;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userModel = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(userModel);
    }

    public UserDetails loadUserById(UUID userId) throws AuthenticationCredentialsNotFoundException {
        UserEntity userModel = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User Not Found with userId: " + userId));
        return UserDetailsImpl.build(userModel);
    }

}
