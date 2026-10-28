package edu.bsit.userhub.alibong.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.security.auth.message.AuthException;

@RestControllerAdvice 
public class ApiExceptionHandler {
    @ExceptionHandler 
    (AuthException.class)
    @ResponseStatus 
    (HttpStatus.UNAUTHORIZED)

    public Map<String, String> auth(AuthException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler 
    (ForbiddenException.class)
    @ResponseStatus 
    (HttpStatus.FORBIDDEN)
    public Map<String, String> denied(ForbiddenException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    (IllegalArgumentException.class)
    @ResponseStatus 
    (HttpStatus.CONFLICT)
    public Map<String, String> bad(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }
    
}
