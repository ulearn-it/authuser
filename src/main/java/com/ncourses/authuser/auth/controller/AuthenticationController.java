package com.ncourses.authuser.auth.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ncourses.authuser.auth.dtos.JwtDto;
import com.ncourses.authuser.auth.dtos.LoginDto;
import com.ncourses.authuser.auth.service.AuthenticationService;
import com.ncourses.authuser.config.security.JwtProvider;
import com.ncourses.authuser.user.model.dtos.UserDto;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {

    AuthenticationManager authenticationManager;
    AuthenticationService authenticationService;
    JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
                                               @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerUserStudent(userDto));
    }

    @PostMapping("/signup/admin/usr")
    public ResponseEntity<Object> registerUserAdmin(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
                                                    @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerUserAdmin(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }

}
