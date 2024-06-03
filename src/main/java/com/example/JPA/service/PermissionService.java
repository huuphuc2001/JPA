package com.example.JPA.service;

import com.example.JPA.dto.request.PermissionRequest;
import com.example.JPA.dto.respone.PermissionRepone;
import com.example.JPA.entity.Permission;
import com.example.JPA.mapper.PermissionMapper;
import com.example.JPA.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionService {
    @Autowired
    PermissionRepository repository;
    @Autowired
    PermissionMapper mapper;
    public PermissionRepone create(PermissionRequest request){
        Permission permission = mapper.toPermission(request);
        permission = repository.save(permission);
        return mapper.toPermissionRepone(permission);
    }
    public List<PermissionRepone> getAll(){
        var permission = repository.findAll();
        return permission.stream().map(mapper::toPermissionRepone).toList();
    }
    public void  delete(String permission){
        repository.deleteById(permission);
    }
}
