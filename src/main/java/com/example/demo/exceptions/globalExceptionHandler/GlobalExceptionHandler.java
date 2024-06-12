package com.example.demo.exceptions.globalExceptionHandler;

import com.example.demo.exceptions.ApplicationExceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationExceptions.class)
    public final ResponseEntity<?> HandleException(ApplicationExceptions ex){
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<?> HandleException(Throwable ex){
        return ResponseEntity.internalServerError().body("Unknown Error");
    }
}
