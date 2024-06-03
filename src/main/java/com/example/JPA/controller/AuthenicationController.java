package com.example.JPA.controller;

import com.example.JPA.dto.respone.ApiRespone;
import com.example.JPA.dto.request.AuthenicationRequest;
import com.example.JPA.dto.request.VerifyTokenRequest;
import com.example.JPA.dto.respone.AuthenicationRespone;
import com.example.JPA.dto.respone.VerifyTokenRespone;
import com.example.JPA.service.AuthenicationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenicationController {
    AuthenicationService service;
@PostMapping("/login")
    ApiRespone<AuthenicationRespone> authenicationResponeApiRespone(@RequestBody AuthenicationRequest request){
    var result = service.authenicated(request);
    return ApiRespone.<AuthenicationRespone>builder()
            .result(result)
            .build();
}
    @PostMapping("/verifytoken")
    ApiRespone<VerifyTokenRespone> verifyTokenResponeApiRespone(@RequestBody VerifyTokenRequest request) throws ParseException, JOSEException {
        var result = service.verifyTokenRespone(request);
        return ApiRespone.<VerifyTokenRespone>builder()
                .result(result)
                .build();
    }
}
