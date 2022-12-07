package com.ncourses.authuser.user.service;

import com.ncourses.authuser.exception.ResourceNotFoundException;
import com.ncourses.authuser.test.builder.TestUserEntityBuilder;
import com.ncourses.authuser.test.util.TestRandomUtils;
import com.ncourses.authuser.user.model.UserDetailsImpl;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserDetailsServiceImplTest {

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class LoadUserByUsername {

        @InjectMocks
        UserDetailsServiceImpl userDetailsService;

        @Mock
        UserRepository userRepository;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldFindUserByUsernameAndMapToUserDetailsImpl() {
            final UserEntity userEntity = TestUserEntityBuilder.builder().build();
            final List<GrantedAuthority> authorities = userEntity.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                    .collect(Collectors.toList());

            when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));

            final UserDetails result = userDetailsService.loadUserByUsername(userEntity.getUsername());

            assertThat(result)
                    .isInstanceOf(UserDetailsImpl.class)
                    .satisfies(user -> {
                        assertThat(user.getUsername()).isEqualTo(userEntity.getUsername());
                        assertThat(user.getPassword()).isEqualTo(userEntity.getPassword());
                        assertThat(user.getAuthorities()).isEqualTo(authorities);
                    });
        }

        @Test
        void shouldThrowUsernameNotFoundExceptionWhenUserNotFoundByUsername() {
            final String userNameNonExistent = TestRandomUtils.randomString();
            when(userRepository.findByUsername(userNameNonExistent)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userDetailsService.loadUserByUsername(userNameNonExistent))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("User Not Found with username: " + userNameNonExistent);
        }

    }

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class LoadUserById {

        @InjectMocks
        UserDetailsServiceImpl userDetailsService;

        @Mock
        UserRepository userRepository;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldFindUserByIdAndMapToUserDetailsImpl() {
            final UserEntity userEntity = TestUserEntityBuilder.builder().build();
            final List<GrantedAuthority> authorities = userEntity.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                    .collect(Collectors.toList());

            when(userRepository.findById(userEntity.getUserId())).thenReturn(Optional.of(userEntity));

            final UserDetails result = userDetailsService.loadUserById(userEntity.getUserId());

            assertThat(result)
                    .isInstanceOf(UserDetailsImpl.class)
                    .satisfies(user -> {
                        assertThat(user.getUsername()).isEqualTo(userEntity.getUsername());
                        assertThat(user.getPassword()).isEqualTo(userEntity.getPassword());
                        assertThat(user.getAuthorities()).isEqualTo(authorities);
                    });
        }

        @Test
        void shouldThrowAuthenticationCredentialsNotFoundExceptionWhenUserNotFoundById() {
            final UUID userIdNonExistent = UUID.randomUUID();
            when(userRepository.findById(userIdNonExistent)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userDetailsService.loadUserById(userIdNonExistent))
                    .isInstanceOf(AuthenticationCredentialsNotFoundException.class)
                    .hasMessage("User Not Found with userId: " + userIdNonExistent);
        }

    }

}