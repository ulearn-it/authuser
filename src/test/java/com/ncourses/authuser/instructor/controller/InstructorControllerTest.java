package com.ncourses.authuser.instructor.controller;

import com.ncourses.authuser.instructor.model.dto.InstructorDto;
import com.ncourses.authuser.instructor.service.InstructorService;
import com.ncourses.authuser.test.builder.TestUserDtoBuilder;
import com.ncourses.authuser.test.util.TestRandomUtils;
import com.ncourses.authuser.user.model.dtos.UserDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class InstructorControllerTest {

    @Nested
    @FieldDefaults(level = AccessLevel.PRIVATE)
    class SaveSubscriptionInstructor {

        @InjectMocks
        InstructorController instructorController;

        @Mock
        InstructorService instructorService;

        @BeforeEach
        void setUp() {
            openMocks(this);
        }

        @Test
        void shouldCallSaveSubscriptionInstructorMethod() {
            final InstructorDto input = TestRandomUtils.randomObject(InstructorDto.class);
            final UserDto expectedInstructor = TestUserDtoBuilder.builder().build();

            when(instructorService.saveSubscriptionInstructor(input)).thenReturn(expectedInstructor);

            final ResponseEntity<UserDto> result = instructorController.saveSubscriptionInstructor(input);

            assertThat(result)
                    .satisfies(response -> {
                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                        assertThat(response.getBody()).isEqualTo(expectedInstructor);
                    });
        }
    }
}