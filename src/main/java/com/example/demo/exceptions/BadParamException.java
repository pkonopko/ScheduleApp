package com.example.demo.exceptions;

import com.example.demo.exceptions.ApplicationExceptions;
import org.springframework.http.HttpStatus;

public class BadParamException extends ApplicationExceptions {
    public BadParamException() {
        super("Bad param were given", HttpStatus.BAD_REQUEST);
    }

}
