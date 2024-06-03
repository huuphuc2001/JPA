package com.example.JPA.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode12 {
    UNDEFINE_EXCPTION(9999,"undeffine exception", HttpStatus.INTERNAL_SERVER_ERROR ),
    USER_EXISTED(1002,"user existed",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005,"user not existed",HttpStatus.NOT_FOUND),
    INVALID_KEY(1002,"invalid message key",HttpStatus.BAD_REQUEST),
    USER_INVALID(1003,"username more than 3 charact",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "invalid password",HttpStatus.BAD_REQUEST),
    UNAUTHENICATED(1006,"not authenicated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"you do not have permission",HttpStatus.FORBIDDEN)

    ;
    private int code;
    private String message;
    //bài 11 quản lý Exception -> trả về respone tuỳ biến
    private HttpStatusCode statusCode;
    ErrorCode12(int code, String message,HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
