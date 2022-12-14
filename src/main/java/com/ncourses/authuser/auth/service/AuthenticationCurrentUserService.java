package com.ncourses.authuser.auth.service;

import com.ncourses.authuser.role.model.enums.RoleType;
import com.ncourses.authuser.user.model.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationCurrentUserService {

    public UserDetailsImpl getCurrentUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public boolean currentUserHasRole(RoleType roleType) {
        return getCurrentUser().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(x -> x.equals(roleType.toString()));
    }
}
