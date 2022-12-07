package com.ncourses.authuser.user.service;

import com.ncourses.authuser.exception.ResourceNotFoundException;
import com.ncourses.authuser.test.builder.TestUserEntityBuilder;
import com.ncourses.authuser.test.util.TestRandomUtils;
import com.ncourses.authuser.user.constants.UserConstants;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class FindById {

        @InjectMocks
        UserService userService;

        @Mock
        UserRepository userRepository;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldCallRepositoryFindUserByIdAndReturnUserEntity() {
            final UserEntity userEntity = TestUserEntityBuilder.builder().build();

            when(userRepository.findById(userEntity.getUserId())).thenReturn(Optional.of(userEntity));

            final UserEntity result = userService.findById(userEntity.getUserId());

            verify(userRepository, only()).findById(userEntity.getUserId());
            assertThat(result).isEqualTo(userEntity);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenUserNotFoundById() {
            final UUID uuidNonExistent = UUID.randomUUID();
            when(userRepository.findById(uuidNonExistent)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findById(uuidNonExistent))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(MessageFormat.format(UserConstants.USER_NOT_FOUND_WITH_ID, uuidNonExistent));
        }
    }

}