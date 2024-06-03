package com.example.JPA.controller;

import com.example.JPA.dto.request.RoleRequest;
import com.example.JPA.dto.respone.ApiRespone;
import com.example.JPA.dto.respone.PermissionRepone;
import com.example.JPA.dto.respone.RoleResponse;
import com.example.JPA.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService service;
    @PostMapping
    ApiRespone<RoleResponse> create(@RequestBody RoleRequest request){
        var response = service.create(request);
        return ApiRespone.<RoleResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    ApiRespone<List<RoleResponse>> getAll(){
        return ApiRespone.<List<RoleResponse>>builder()
                .result(service.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiRespone<Void> delete(@PathVariable String role){
        service.delete(role);
        return ApiRespone.<Void>builder().build();
    }
}
