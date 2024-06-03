package com.example.JPA.service;

import com.example.JPA.dto.request.RoleRequest;
import com.example.JPA.dto.respone.RoleResponse;
import com.example.JPA.entity.Permission;
import com.example.JPA.entity.Role;
import com.example.JPA.mapper.RoleMapper;
import com.example.JPA.repository.PermissionRepository;
import com.example.JPA.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoleService {
    RoleRepository repository;
    PermissionRepository permissionRepository;
    RoleMapper mapper;
    public RoleResponse create(RoleRequest request){
        //map request về entity
        // == var role = mapper.toRole(request);
        Role role = mapper.toRole(request);
        //tiếp theo lấy list các permission
       var permissions = permissionRepository.findAllById(request.getPermissions());
       role.setPermissions(new HashSet<>(permissions));
       role =  repository.save(role);
       return mapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        var roles = repository.findAll();

        return roles.stream().map(mapper::toRoleResponse).toList();
    }

    public void delete(String role){
        repository.deleteById(role);
    }
}
