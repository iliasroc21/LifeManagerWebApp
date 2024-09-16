package org.lifemanager.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SystemExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({EntityNotFoundException.class , EntityAlreadyExistsException.class})
    public ResponseEntity<ErrorWrapper> handleEntityNotFoundException (Exception ex){
        return new ResponseEntity<>(new ErrorWrapper(ex.getMessage() ) , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorWrapper> handleRefreshTokenExpiredException(Exception ex){
        return new ResponseEntity<>(new ErrorWrapper(ex.getMessage() ) , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<ErrorWrapper> handleVerificationException(Exception ex){
        return new ResponseEntity<>(new ErrorWrapper(ex.getMessage() ) , HttpStatus.BAD_REQUEST);
    }
}
