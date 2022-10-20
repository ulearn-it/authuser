package com.ncourses.authuser.auth.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@RequiredArgsConstructor
public class JwtDto {

    @NonNull
    private String token;
    private String type = "Bearer";

}
