package com.ncourses.authuser.exception.dto;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardError implements Serializable {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

}