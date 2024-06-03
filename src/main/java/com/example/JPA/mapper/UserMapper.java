package com.example.JPA.mapper;


import com.example.JPA.dto.request.UserCreationRepest;
import com.example.JPA.dto.request.UserUpdateRepest;
import com.example.JPA.dto.respone.UserRespone;
import com.example.JPA.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

//báo cho mapstruct biết mapper sẽ sử dụng trong spring
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRepest repest);
    UserRespone toUserResponse(User user);
    //map request vào object user

    //với @MappingTarget sẽ sao chép đối tượng request sang user
    //đối tượng dc đánh dấu là đối tượng  dc thay thế cái mới
    @Mapping(target = "roles",ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRepest request);
}
