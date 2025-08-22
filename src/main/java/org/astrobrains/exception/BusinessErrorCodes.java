package org.astrobrains.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {

    // Generic / fallback
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No Code"),
    INTERNAL_ERROR(9999, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"),

    // Auth-related errors (1xx)
    BAD_CREDENTIALS(100, HttpStatus.UNAUTHORIZED, "Bad credentials"),
    INCORRECT_CURRENT_PASSWORD(101, HttpStatus.BAD_REQUEST, "Incorrect current password"),
    NEW_PASSWORD_DOES_NOT_MATCH(102, HttpStatus.BAD_REQUEST, "New password does not match"),
    ACCOUNT_DISABLED(103, HttpStatus.UNAUTHORIZED, "User account is disabled"),
    ACCOUNT_LOCKED(104, HttpStatus.FORBIDDEN, "User account is locked"),

    // Validation errors (4xx)
    VALIDATION_ERROR(4001, HttpStatus.BAD_REQUEST, "Validation failed");

    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
