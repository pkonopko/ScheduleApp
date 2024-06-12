package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class StudentNotFoundExceptions extends ApplicationExceptions {
    public StudentNotFoundExceptions() {
        super("No student with given id", HttpStatus.NOT_FOUND);
    }
}
