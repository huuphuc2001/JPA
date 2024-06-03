package com.example.JPA.configuration;

import com.example.JPA.dto.respone.ApiRespone;
import com.example.JPA.exception.ErrorCode12;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    //xử lý lỗi 401 | bài 11
    //xảy ra khi 1 exception xảy ra trong quá trình authenticate/ thường là authenticate ko thành công
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode12 errorCode12 = ErrorCode12.UNAUTHENICATED;

        response.setStatus(errorCode12.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);//trả dữ liệu về kiểu gì
        //respone body
        ApiRespone<?> apiRespone = ApiRespone.builder()
                .code(errorCode12.getCode())
                .message(errorCode12.getMessage())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiRespone));
        response.flushBuffer();
    }
}
