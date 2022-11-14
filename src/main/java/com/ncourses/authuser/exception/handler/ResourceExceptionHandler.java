package com.ncourses.authuser.exception.handler;

import com.ncourses.authuser.exception.MismatchedPasswordException;
import com.ncourses.authuser.exception.ResourceNotFoundException;
import com.ncourses.authuser.exception.UniqueFieldException;
import com.ncourses.authuser.exception.dto.StandardError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public StandardError notFound(ResourceNotFoundException e, HttpServletRequest request) {
        return StandardError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource not found")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(MismatchedPasswordException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public StandardError mismatchedPassword(MismatchedPasswordException e, HttpServletRequest request) {
        return StandardError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Mismatched password")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(UniqueFieldException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public StandardError uniqueField(UniqueFieldException e, HttpServletRequest request) {
        return StandardError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Unique field")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }
}
