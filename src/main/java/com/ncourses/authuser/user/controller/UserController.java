package com.ncourses.authuser.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ncourses.authuser.auth.service.AuthenticationCurrentUserService;
import com.ncourses.authuser.specification.SpecificationTemplate;
import com.ncourses.authuser.user.model.UserDetailsImpl;
import com.ncourses.authuser.user.model.UserEntity;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.service.UserService;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    UserService userService;

    AuthenticationCurrentUserService authenticationCurrentUserService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserEntity>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                        @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                        Authentication authentication) {
        final UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Authentication {}", userDetails.getUsername());
        Page<UserEntity> userModelPage = userService.findAll(spec, pageable)
                .map(user -> user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel()));
        return ResponseEntity.ok().body(userModelPage);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId) {
        final UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if (!currentUserId.equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }
        return ResponseEntity.ok().body(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") UUID userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        return ResponseEntity.ok().body(userService.updateUser(userId, userDto));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                 @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        return ResponseEntity.ok().body(userService.updatePassword(userId, userDto));
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                              @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
        return ResponseEntity.ok().body(userService.updateImage(userId, userDto));
    }

}
