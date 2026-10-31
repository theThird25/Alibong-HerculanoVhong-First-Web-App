package edu.bsit.userhub.alibong.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends ApiException {
    public AuthException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
        {

        }
    }

}
