package com.example.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class TeacherNotFoundExceptions extends ApplicationExceptions {
    public TeacherNotFoundExceptions() {
        super("No teacher with given id", HttpStatus.NOT_FOUND);

    }
}
