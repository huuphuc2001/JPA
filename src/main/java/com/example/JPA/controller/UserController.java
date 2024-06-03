package com.example.JPA.controller;

import com.example.JPA.dto.respone.ApiRespone;
import com.example.JPA.dto.request.UserCreationRepest;
import com.example.JPA.dto.request.UserUpdateRepest;
import com.example.JPA.dto.respone.UserRespone;
import com.example.JPA.entity.User;
import com.example.JPA.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    ApiRespone<User> createUser(@RequestBody @Valid UserCreationRepest request){
        ApiRespone<User> respone = new ApiRespone<>();
//        respone.setResult(userService.createUser(request));
//        return respone;
        return ApiRespone.<User>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
//    List<User> getUser(){
//
//        var authenication = SecurityContextHolder.getContext().getAuthentication();
//        //khi api getuser đc gọi thì sẽ log ra user đang đăng nhập hiện tại và có những role j
//        log.info("Username: {}", authenication.getName());
//        authenication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
//        return userService.getUser();
//    }
    ApiRespone<List<User>> getUser(){
        var authenication = SecurityContextHolder.getContext().getAuthentication();
        //khi api getuser đc gọi thì sẽ log ra user đang đăng nhập hiện tại và có những role j
        log.info("Username: {}", authenication.getName());
        authenication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));


        return ApiRespone.<List<User>>builder()
                .result(userService.getUser())
                .build();
    }
    ;

    @GetMapping("/{userId}")
    User getUser(@PathVariable String userId){
        return userService.getUserId(userId);
    }

    @GetMapping("/myInfo")
    ApiRespone<UserRespone> getUserInfo(){
        return ApiRespone.<UserRespone>builder()
                .result(userService.getMyInfo())
                .build();
    }


    @PutMapping("/{userId}")
//    User updateUser(@PathVariable String userId,@RequestBody UserUpdateRepest request){
//        return userService.updateUserId(userId, request);
//    }
    ApiRespone<UserRespone> updateUser(@PathVariable String userId,@RequestBody UserUpdateRepest request){
        return ApiRespone.<UserRespone>builder()
                .result(userService.updateUser(userId,request))
                .build();
    }
}
