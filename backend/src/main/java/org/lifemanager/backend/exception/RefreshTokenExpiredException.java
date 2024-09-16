package org.lifemanager.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException(String message){
        super(message);
    }
}
