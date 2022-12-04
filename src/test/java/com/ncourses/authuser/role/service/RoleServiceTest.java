package com.ncourses.authuser.role.service;

import com.ncourses.authuser.exception.ResourceNotFoundException;
import com.ncourses.authuser.role.model.RoleEntity;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.role.repository.RoleRepository;
import com.ncourses.authuser.test.builder.TestRoleEntityBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RoleServiceTest {

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class FindByRoleName {

        @InjectMocks
        RoleService roleService;

        @Mock
        RoleRepository roleRepository;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldFindRoleByName() {
            final RoleType input = RoleType.ROLE_USER;
            final RoleEntity expectedRole = TestRoleEntityBuilder.builder().build();

            when(roleRepository.findByRoleName(input)).thenReturn(Optional.of(expectedRole));

            final RoleEntity result = roleService.findByRoleName(input);

            assertThat(result).isEqualTo(expectedRole);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenRoleNotFound() {
            final RoleType input = RoleType.ROLE_USER;

            when(roleRepository.findByRoleName(input)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> roleService.findByRoleName(input))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Role not found: " + input.name());
        }
    }

}