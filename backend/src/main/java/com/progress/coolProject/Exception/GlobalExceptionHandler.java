package com.progress.coolProject.Exception;

import com.progress.coolProject.DTO.ResponseDTO;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Data integrity violation: Possible duplicate entry or constraint violation. " + ex.getMessage()),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    public ResponseEntity<ResponseDTO> handleUnexpectedRollbackException(UnexpectedRollbackException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Transaction rolled back: " + ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseDTO> handleDataAccessException(DataAccessException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Database error: " + ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ResponseDTO.error(ex.getMessage()),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ResponseDTO> handleDuplicateKeyException(DuplicateKeyException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Duplicate key error: " + ex.getMessage()),
            HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<ResponseDTO> handleInvocationTargetException(InvocationTargetException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Invocation target error: " + ex.getTargetException().getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ResponseDTO> handleClassCastException(ClassCastException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Class cast error: " + ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Invalid credentials: " + ex.getMessage()),
            HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseDTO> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return new ResponseEntity<>(ResponseDTO.error("Access denied: " + ex.getMessage()),
            HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseDTO> handleJwtException(JwtException ex) {
        return new ResponseEntity<>(ResponseDTO.error("JWT processing error: " + ex.getMessage()),
            HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseDTO> handleGeneralException(IOException ex) {
        return new ResponseEntity<>(ResponseDTO.internalError("An unexpected error occurred: " + ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ResponseDTO.notFound(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ResponseDTO.error(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ResponseDTO> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return new ResponseEntity<>(ResponseDTO.error(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
