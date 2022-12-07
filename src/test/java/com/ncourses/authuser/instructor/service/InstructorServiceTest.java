package com.ncourses.authuser.instructor.service;

import com.ncourses.authuser.instructor.model.dto.InstructorDto;
import com.ncourses.authuser.role.model.RoleEntity;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.role.service.RoleService;
import com.ncourses.authuser.test.builder.TestRoleEntityBuilder;
import com.ncourses.authuser.test.builder.TestUserDtoBuilder;
import com.ncourses.authuser.test.builder.TestUserEntityBuilder;
import com.ncourses.authuser.test.util.TestRandomUtils;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.model.enums.UserType;
import com.ncourses.authuser.user.model.mapper.UserMapper;
import com.ncourses.authuser.user.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class InstructorServiceTest {

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class SaveSubscriptionInstructor {

        @InjectMocks
        InstructorService instructorService;

        @Mock
        RoleService roleService;
        @Mock
        UserService userService;
        @Mock
        UserMapper userMapper;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }


        @Test
        void shouldFindUserAndSetUserTypeInstructorAndReturnUserDto() {
            final InstructorDto input = TestRandomUtils.randomObject(InstructorDto.class);
            final UserEntity userEntity = TestUserEntityBuilder.builder().build();
            final RoleEntity roleModel = TestRoleEntityBuilder.builder().build();
            final UserDto userDto = TestUserDtoBuilder.builder().withUserType(UserType.INSTRUCTOR).build();

            when(userService.findById(input.getUserId())).thenReturn(userEntity);
            when(roleService.findByRoleName(RoleType.ROLE_INSTRUCTOR)).thenReturn(roleModel);
            when(userMapper.userToUserDto(userEntity)).thenReturn(userDto);

            final UserDto result = instructorService.saveSubscriptionInstructor(input);

            assertThat(result)
                    .satisfies(user -> {
                        assertThat(user.getUserId()).isEqualTo(userDto.getUserId());
                        assertThat(user.getUserType()).isEqualTo(UserType.INSTRUCTOR);
                    });
        }

    }

}