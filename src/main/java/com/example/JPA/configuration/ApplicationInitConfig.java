package com.example.JPA.configuration;

import com.example.JPA.entity.User;
import com.example.JPA.enums.Role;
import com.example.JPA.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;


//thông tin role phải có trong token mới có thể phân quyền
//class này sẽ tạo 1 user admin khi mình start class này lên
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
//    @Autowired
    //lombok sẽ tự tạo constructor và Bean tự động inject vào constructor đó
    //nên ko cần Autowired
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            //application có thể stop/start nhìu lần
            //kt xem user admin này tồn tại hay chưa - nếu chưa thì mới chạy
            if(userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add("ROLE_" + Role.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
//                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin");
            }
        };
    }
}
