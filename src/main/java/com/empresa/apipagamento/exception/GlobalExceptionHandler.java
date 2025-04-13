package com.empresa.apipagamento.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Data
    public static class ApiError {
        private HttpStatus status;
        private String message;
        private LocalDateTime timestamp;
        private Map<String, String> errors;

        public ApiError(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
            this.timestamp = LocalDateTime.now();
            this.errors = new HashMap<>();
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed");
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> 
            error.getErrors().put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed");
        ex.getConstraintViolations().forEach(violation -> 
            error.getErrors().put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiError error = new ApiError(HttpStatus.CONFLICT, "Database constraint violation");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}