package com.example.JPA.mapper;


import com.example.JPA.dto.request.PermissionRequest;
import com.example.JPA.dto.request.UserCreationRepest;
import com.example.JPA.dto.request.UserUpdateRepest;
import com.example.JPA.dto.respone.PermissionRepone;
import com.example.JPA.dto.respone.UserRespone;
import com.example.JPA.entity.Permission;
import com.example.JPA.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

//báo cho mapstruct biết mapper sẽ sử dụng trong spring
@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest repest);
    PermissionRepone toPermissionRepone(Permission permission);

}
