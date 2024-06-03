package com.example.JPA.mapper;


import com.example.JPA.dto.request.PermissionRequest;
import com.example.JPA.dto.request.RoleRequest;
import com.example.JPA.dto.respone.PermissionRepone;
import com.example.JPA.dto.respone.RoleResponse;
import com.example.JPA.entity.Permission;
import com.example.JPA.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//báo cho mapstruct biết mapper sẽ sử dụng trong spring
@Mapper(componentModel = "spring")
public interface RoleMapper {
    //khi map sẽ bỏ qua attribute "permissions" trong Role
    //
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);


    //public RoleResponse toRoleResponse(Role role) {
    //        return new RoleResponse(role.getId(), role.getName());
    //    }
}
