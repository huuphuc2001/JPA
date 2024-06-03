package com.example.JPA.controller;

import com.example.JPA.dto.request.PermissionRequest;
import com.example.JPA.dto.respone.ApiRespone;
import com.example.JPA.dto.respone.PermissionRepone;
import com.example.JPA.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService service;
    @PostMapping
    ApiRespone<PermissionRepone> create(@RequestBody PermissionRequest request){
        return ApiRespone.<PermissionRepone>builder()
                .result(service.create(request))
                .build();
    }
    @GetMapping
    ApiRespone<List<PermissionRepone>> getAll(){
        return ApiRespone.<List<PermissionRepone>>builder()
                .result(service.getAll())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiRespone<Void> delete(@PathVariable String permission){
        service.delete(permission);
        return ApiRespone.<Void>builder().build();
    }

}
