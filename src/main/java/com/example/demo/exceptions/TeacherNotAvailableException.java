package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class TeacherNotAvailableException extends ApplicationExceptions{
    public TeacherNotAvailableException() {
        super("Teacher not available", HttpStatus.BAD_REQUEST);
    }
}

