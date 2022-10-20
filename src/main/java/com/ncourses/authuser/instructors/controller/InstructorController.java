package com.ncourses.authuser.instructors.controller;

import com.ncourses.authuser.dtos.InstructorDto;
import com.ncourses.authuser.role.model.RoleModel;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.role.service.RoleService;
import com.ncourses.authuser.user.model.UserModel;
import com.ncourses.authuser.user.model.enums.UserType;
import com.ncourses.authuser.user.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto) {
        Optional<UserModel> userModelOptional = userService.findById(instructorDto.getUserId());
        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else {
            RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_INSTRUCTOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is Not Found."));
            var userModel = userModelOptional.get();
            userModel.setUserType(UserType.INSTRUCTOR);
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userModel.getRoles().add(roleModel);
            userService.updateUser(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

}
