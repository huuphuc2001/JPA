package com.example.JPA.exception;

import com.example.JPA.dto.respone.ApiRespone;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
//nơi hiển thị error
//thông báo cho spring biết đây là nơi handle exeption dùng @ControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandle {
    //define các loại exception các bạn bắt ở đây
    //khi có bất cứ RuntimeException xảy ra trên hệ thống thì đều đụược xử lý ở đây
    //class này sẽ bắt những exception nằm ngoài những exception đã define
    //ResponseEntity
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiRespone> handleRuntimeException(RuntimeException exception){
        ApiRespone apiRespone = new ApiRespone();
        apiRespone.setCode(ErrorCode12.UNDEFINE_EXCPTION.getCode());
        apiRespone.setMessage(ErrorCode12.UNDEFINE_EXCPTION.getMessage());
        return ResponseEntity.badRequest().body(apiRespone);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiRespone> handleAppException(AppException exception){
        ErrorCode12 errorCode12 = exception.getErrorCode12();
        ApiRespone apiRespone = new ApiRespone();
        apiRespone.setCode(errorCode12.getCode());
        apiRespone.setMessage(errorCode12.getMessage());
        return ResponseEntity.status(errorCode12.getStatusCode()).body(apiRespone);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiRespone> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        String enumkey = exception.getFieldError().getDefaultMessage();
        ErrorCode12 errorCode12 = ErrorCode12.INVALID_KEY;
        try {
            errorCode12 = ErrorCode12.valueOf(enumkey);
        }catch (IllegalArgumentException e){

        }
        ApiRespone respone = new ApiRespone();
        respone.setCode(errorCode12.getCode());
        respone.setMessage(errorCode12.getMessage());

        return ResponseEntity.badRequest().body(respone);
    }

    //User ko có quyền truy cập endpoint
    @ExceptionHandler(value = AccessDeniedException.class)
     ResponseEntity<ApiRespone> handlingAccessdeniedException(AccessDeniedException e){
        ErrorCode12 errorCode12 = ErrorCode12.UNAUTHORIZED;
        return ResponseEntity.status(errorCode12.getStatusCode()).body(
                ApiRespone.builder()
                        .code(errorCode12.getCode())
                        .message(errorCode12.getMessage())
                        .build()
        );
    }
}
