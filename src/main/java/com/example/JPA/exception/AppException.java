package com.example.JPA.exception;

public class AppException extends RuntimeException{
    private ErrorCode12 errorCode12;

    public AppException(ErrorCode12 errorCode12) {
        super(errorCode12.getMessage());
        this.errorCode12 = errorCode12;
    }

    public ErrorCode12 getErrorCode12() {
        return errorCode12;
    }

    public void setErrorCode(ErrorCode12 errorCode12) {
        this.errorCode12 = errorCode12;
    }
}
