package org.astrobrains.exception;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom AppException
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponse> handleAppException(AppException ex, HttpServletRequest request) {
        var errorCode = ex.getErrorCode();
        log.error("AppException [{}] at [{}]: {}", errorCode.getCode(), request.getRequestURI(), ex.getMessage());
        return buildResponse(errorCode, request.getRequestURI(), null);
    }

    // Handle Spring Security exceptions
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildResponse(BusinessErrorCodes.BAD_CREDENTIALS, request.getRequestURI(), null);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleDisabled(DisabledException ex, HttpServletRequest request) {
        return buildResponse(BusinessErrorCodes.ACCOUNT_DISABLED, request.getRequestURI(), null);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleLocked(LockedException ex, HttpServletRequest request) {
        return buildResponse(BusinessErrorCodes.ACCOUNT_LOCKED, request.getRequestURI(), null);
    }

    // Handle Messaging exception
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessaging(MessagingException ex, HttpServletRequest request) {
        log.error("MessagingException at [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(BusinessErrorCodes.INTERNAL_ERROR, request.getRequestURI(), null);
    }

    // Handle Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return buildResponse(BusinessErrorCodes.VALIDATION_ERROR, request.getRequestURI(), errors);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Unexpected exception at [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(BusinessErrorCodes.INTERNAL_ERROR, request.getRequestURI(), null);
    }

    // Utility method to build consistent response
    private ResponseEntity<ExceptionResponse> buildResponse(BusinessErrorCodes errorCode, String path, Map<String, String> errors) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .timestamp(Instant.now())
                        .status(errorCode.getHttpStatus().value())
                        .code(errorCode.getCode())
                        .message(errorCode.getDescription())
                        .path(path)
                        .errors(errors)
                        .build());
    }
}
