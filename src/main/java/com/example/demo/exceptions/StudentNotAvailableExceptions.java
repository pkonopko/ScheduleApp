package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class StudentNotAvailableExceptions extends ApplicationExceptions{
    public StudentNotAvailableExceptions() {
        super("Student not available", HttpStatus.BAD_REQUEST);
    }
}