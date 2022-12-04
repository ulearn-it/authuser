package com.ncourses.authuser.instructor.controller;

import com.ncourses.authuser.instructor.model.dto.InstructorDto;
import com.ncourses.authuser.instructor.service.InstructorService;
import com.ncourses.authuser.user.model.dtos.UserDto;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InstructorController {

    InstructorService instructorService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/subscription")
    public ResponseEntity<UserDto> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto) {
        return ResponseEntity.ok().body(instructorService.saveSubscriptionInstructor(instructorDto));
    }
}
