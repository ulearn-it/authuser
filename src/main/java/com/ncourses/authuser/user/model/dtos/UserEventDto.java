package com.ncourses.authuser.user.model.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UserEventDto {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String imageUrl;
    private String actionType;

}
