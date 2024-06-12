package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class TimeException extends ApplicationExceptions {
    public TimeException() {
        super("Hour is to low", HttpStatus.BAD_REQUEST);
    }
}
