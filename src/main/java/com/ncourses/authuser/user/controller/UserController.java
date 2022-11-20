package com.ncourses.authuser.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ncourses.authuser.auth.service.AuthenticationCurrentUserService;
import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.specification.SpecificationTemplate;
import com.ncourses.authuser.user.model.UserDetailsImpl;
import com.ncourses.authuser.user.model.dtos.UserDto;
import com.ncourses.authuser.user.model.dtos.UserListDto;
import com.ncourses.authuser.user.model.mapper.UserMapper;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;
    AuthenticationCurrentUserService authenticationCurrentUserService;
    UserMapper userMapper;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserListDto>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                         @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                         Authentication authentication) {
        final UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Authentication {}", userDetails.getUsername());
        Page<UserListDto> userPage = userService.findAll(spec, pageable)
                .map(user -> user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel()));
        return ResponseEntity.ok(userPage);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{userId}")
    @JsonView(UserDto.UserView.BasicView.class)
    public ResponseEntity<UserDto> getOneUser(@PathVariable(value = "userId") UUID userId) {
        final UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        final boolean currentUserIsAdmin = authenticationCurrentUserService.currentUserHasRole(RoleType.ROLE_ADMIN);
        if (!currentUserIsAdmin && !currentUserId.equals(userId)) {
            throw new AccessDeniedException("Forbidden");
        }
        UserDto userDto = userMapper.userToUserDto(userService.findById(userId));
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") UUID userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userId, userDto));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<UserDto> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                  @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                 @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        return ResponseEntity.ok().body(userService.updatePassword(userId, userDto));
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<UserDto> updateImage(@PathVariable(value = "userId") UUID userId,
                                               @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                              @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
        return ResponseEntity.ok().body(userService.updateImage(userId, userDto));
    }

}
