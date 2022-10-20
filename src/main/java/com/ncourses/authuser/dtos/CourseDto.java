package com.ncourses.authuser.dtos;

import com.ncourses.authuser.enums.CourseLevel;
import com.ncourses.authuser.enums.CourseStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseDto {

    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;
    private CourseStatus courseStatus;
    private UUID userInstructor;
    private CourseLevel courseLevel;

}
