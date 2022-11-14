package com.ncourses.authuser.exception;

public class MismatchedPasswordException extends RuntimeException {

    public MismatchedPasswordException(String message) {
        super(message);
    }
}
