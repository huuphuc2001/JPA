package com.example.JPA.service;

import com.example.JPA.dto.request.UserCreationRepest;
import com.example.JPA.dto.request.UserUpdateRepest;
import com.example.JPA.dto.respone.UserRespone;
import com.example.JPA.entity.User;
import com.example.JPA.enums.Role;
import com.example.JPA.exception.AppException;
import com.example.JPA.exception.ErrorCode12;

import com.example.JPA.mapper.UserMapper;
import com.example.JPA.repository.RoleRepository;
import com.example.JPA.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
//@RequiredArgsConstructor
@Slf4j
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User createUser(UserCreationRepest reqest){

        if(userRepository.existsByUsername(reqest.getUsername()))
            throw new AppException(ErrorCode12.USER_EXISTED);
        //mapping request vào user
        //ko dùng đến các getter và setter
        User user = userMapper.toUser(reqest);
        //-----------------------------Mã hoá mk-------------------------------------
        //Bcrypt là impl của PasswordEncoder
        //(10) độ mạnh của mã hoá
//      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(reqest.getPassword()));
        //------------------------------------------------------------------
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        user.setRoles(roles);
        return userRepository.save(user);

    }
    @PreAuthorize("hasRole('ADMIN')")//chặn tất cả các role khác admin từ ban đầu
    //kt role trc khi vào method
    //với hashRole nó sed tìm tới authority nào có prefix là ROLE_ADMIN: có ROLE_ trước
    //@PreAuthorize("hasAuthority('CREATE_POST')")
    //khi dùng hasAuthority nó sẽ map chính xác các author có tên như v
    public List<User> getUser(){
        log.info("in method get user");
        return userRepository.findAll();
    }
//    @PostAuthorize("hasRole('ADMIN')")//method đã thực hiện xong sau đó mới check role sau cùng
                                    // nếu ko phải admin thì sẽ ko dc truy cập
    //sau khi method thực hiện xong nếu ko phải role admin thì sẽ bị chặn
    //returnObject là user của mình trả về
    //authentication.name là user đang đăng nhập
    @PostAuthorize("returnObject.username == authentication.name")
    public User getUserId(String id){
        return userRepository.findById(id).orElseThrow(() ->new AppException(ErrorCode12.USER_NOT_EXISTED));
    }

    public UserRespone getMyInfo(){//user tự lấy info của mình
        //trong spring security khi 1 request dc xác thực thành công
        // thì thông tin của user đăng nhập sẽ dc lưu trong Security Context Holder
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode12.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public User updateUserId(String userId, UserUpdateRepest request){

        User user = getUserId(userId);
        userMapper.updateUser(user,request);
        return userRepository.save(user);
    }

    public UserRespone updateUser(String userId, UserUpdateRepest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode12.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
