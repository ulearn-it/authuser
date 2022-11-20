package com.ncourses.authuser.user.controller;

import com.ncourses.authuser.auth.service.AuthenticationCurrentUserService;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.specification.SpecificationTemplate;
import com.ncourses.authuser.test.builder.TestUserDtoBuilder;
import com.ncourses.authuser.test.builder.TestUserEntityBuilder;
import com.ncourses.authuser.test.builder.TestUserListDTOBuilder;
import com.ncourses.authuser.user.model.UserDetailsImpl;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.model.dtos.UserListDto;
import com.ncourses.authuser.user.model.enums.UserType;
import com.ncourses.authuser.user.model.mapper.UserMapper;
import com.ncourses.authuser.user.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserControllerTest {

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class GetAllUsersTest {

        @InjectMocks
        UserController userController;

        @Mock
        UserService userService;
        @Mock
        Authentication authentication;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldCallFindAllServiceMethodAndAddLinkTo() {
            final GrantedAuthority admin = new SimpleGrantedAuthority(UserType.ADMIN.toString());
            final UserDetails userDetails = UserDetailsImpl.builder()
                    .userId(UUID.randomUUID())
                    .fullName("Full name")
                    .username("username")
                    .password("password")
                    .email("email")
                    .authorities(List.of(admin))
                    .build();
            final SpecificationTemplate.UserSpec spec = (root, query, criteriaBuilder) -> null;
            final Pageable pageable = PageRequest.of(0, 1, Sort.Direction.ASC, "userId");
            final Page<UserListDto> userListPaged = new PageImpl<>(TestUserListDTOBuilder.listBuilder().buildList());

            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userService.findAll(spec, pageable)).thenReturn(userListPaged);

            final ResponseEntity<Page<UserListDto>> result = userController.getAllUsers(spec, pageable, authentication);

            verify(authentication, times(1)).getPrincipal();
            verify(userService, times(1)).findAll(spec, pageable);
            verifyNoMoreInteractions(authentication, userService);

            assertThat(result)
                    .satisfies(res -> {
                        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                        assertThat(res.getBody()).isEqualTo(userListPaged);
                    })
                    .extracting(HttpEntity::getBody)
                    .extracting(Slice::getContent)
                    .asInstanceOf(InstanceOfAssertFactories.list(UserListDto.class))
                    .allSatisfy(user -> assertThat(user.getLink("self"))
                            .isNotEmpty()
                            .get()
                            .extracting(Link::getHref)
                            .isEqualTo("/users/" + user.getUserId()));
        }
    }

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class GetOneUserTest {

        @InjectMocks
        UserController userController;

        @Mock
        UserService userService;
        @Mock
        UserMapper userMapper;
        @Mock
        AuthenticationCurrentUserService authenticationCurrentUserService;

        UserDetailsImpl userDetails;

        @BeforeEach
        void setUp() {
            openMocks(this);
            userDetails = UserDetailsImpl.builder()
                    .userId(UUID.randomUUID())
                    .fullName("Full name")
                    .username("username")
                    .password("password")
                    .email("email")
                    .authorities(Collections.emptyList())
                    .build();
        }

        @Test
        void shouldThrowAccessDeniedExceptionWhenCurrentUserIsNotAdminAndDoesNotHaveSameInputUUID() {
            final UUID input = UUID.randomUUID();

            when(authenticationCurrentUserService.getCurrentUser()).thenReturn(userDetails);
            when(authenticationCurrentUserService.currentUserHasRole(RoleType.ROLE_ADMIN)).thenReturn(false);

            assertThatThrownBy(() -> userController.getOneUser(input))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("Forbidden");
        }

        @Test
        void shouldRetrieveUserWhenCurrentUserIsAdmin() {
            final UUID input = UUID.randomUUID();
            final UserEntity expectedUser = TestUserEntityBuilder.builder().build();
            final UserDto expectedUserDto = TestUserDtoBuilder.builder().build();

            when(authenticationCurrentUserService.getCurrentUser()).thenReturn(userDetails);
            when(authenticationCurrentUserService.currentUserHasRole(RoleType.ROLE_ADMIN)).thenReturn(true);
            when(userService.findById(input)).thenReturn(expectedUser);
            when(userMapper.userToUserDto(expectedUser)).thenReturn(expectedUserDto);

            final ResponseEntity<UserDto> result = userController.getOneUser(input);

            assertThat(result)
                    .satisfies(res -> {
                        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                        assertThat(res.getBody()).isEqualTo(expectedUserDto);
                    });
        }

        @Test
        void shouldRetrieveUserWhenCurrentUserIsNotAdminButHasSameInputUUID() {
            final UUID input = userDetails.getUserId();
            final UserEntity expectedUser = TestUserEntityBuilder.builder().build();
            final UserDto expectedUserDto = TestUserDtoBuilder.builder().build();

            when(authenticationCurrentUserService.getCurrentUser()).thenReturn(userDetails);
            when(authenticationCurrentUserService.currentUserHasRole(RoleType.ROLE_ADMIN)).thenReturn(false);
            when(userService.findById(input)).thenReturn(expectedUser);
            when(userMapper.userToUserDto(expectedUser)).thenReturn(expectedUserDto);

            final ResponseEntity<UserDto> result = userController.getOneUser(input);

            assertThat(result)
                    .satisfies(res -> {
                        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                        assertThat(res.getBody()).isEqualTo(expectedUserDto);
                    });
        }

    }

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class DeleteUserTest {

        @InjectMocks
        UserController userController;

        @Mock
        UserService userService;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldCallDeleteByIdUserServiceMethod() {
            final UUID input = UUID.randomUUID();

            final ResponseEntity<Void> result = userController.deleteUser(input);

            verify(userService, only()).deleteById(input);

            assertThat(result)
                    .satisfies(res -> {
                        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
                        assertThat(res.getBody()).isNull();
                    });
        }
    }

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class UpdateUserTest {

        @InjectMocks
        UserController userController;

        @Mock
        UserService userService;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldCallUpdateUserServiceMethod() {
            final UUID inputId = UUID.randomUUID();
            final UserDto inputUserDto = TestUserDtoBuilder.builder().build();
            final UserDto updatedUserDto = TestUserDtoBuilder.builder().build();

            when(userService.updateUser(inputId, inputUserDto)).thenReturn(updatedUserDto);

            final ResponseEntity<UserDto> result = userController.updateUser(inputId, inputUserDto);

            verify(userService, only()).updateUser(inputId, inputUserDto);

            assertThat(result)
                    .satisfies(res -> {
                        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                        assertThat(res.getBody()).isEqualTo(updatedUserDto);
                    });
        }
    }

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class UpdatePasswordTest {

        @InjectMocks
        UserController userController;

        @Mock
        UserService userService;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldCallUpdatePasswordUserServiceMethod() {
            final UUID inputId = UUID.randomUUID();
            final UserDto inputUserDto = TestUserDtoBuilder.builder().build();
            final UserDto updatedUserDto = TestUserDtoBuilder.builder().build();

            when(userService.updatePassword(inputId, inputUserDto)).thenReturn(updatedUserDto);

            final ResponseEntity<UserDto> result = userController.updatePassword(inputId, inputUserDto);

            verify(userService, only()).updatePassword(inputId, inputUserDto);

            assertThat(result)
                    .satisfies(res -> {
                        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                        assertThat(res.getBody()).isEqualTo(updatedUserDto);
                    });
        }
    }

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class UpdateImageTest {

        @InjectMocks
        UserController userController;

        @Mock
        UserService userService;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldCallUpdateImageUserServiceMethod() {
            final UUID inputId = UUID.randomUUID();
            final UserDto inputUserDto = TestUserDtoBuilder.builder().build();
            final UserDto updatedUserDto = TestUserDtoBuilder.builder().build();

            when(userService.updateImage(inputId, inputUserDto)).thenReturn(updatedUserDto);

            final ResponseEntity<UserDto> result = userController.updateImage(inputId, inputUserDto);

            verify(userService, only()).updateImage(inputId, inputUserDto);

            assertThat(result)
                    .satisfies(res -> {
                        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
                        assertThat(res.getBody()).isEqualTo(updatedUserDto);
                    });
        }
    }


}