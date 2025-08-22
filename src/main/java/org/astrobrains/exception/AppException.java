package org.astrobrains.exception;

public class AppException extends RuntimeException {

    private final BusinessErrorCodes errorCode;

    public AppException(BusinessErrorCodes errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public BusinessErrorCodes getErrorCode() {
        return errorCode;
    }
}
