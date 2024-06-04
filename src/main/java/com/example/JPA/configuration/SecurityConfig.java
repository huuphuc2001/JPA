package com.example.JPA.configuration;

import com.example.JPA.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //phân quyền cho method
//class cấu hình spring security
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINT = {"/users",
            "/auth/login", "/auth/verifytoken","/auth/logout"
    };
//    @Value("${jwt.signerKey}")
//    private String signerKey;
    @Autowired
    private CustomJwtDecoder customJwtDecoder;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //Phần 1
        //cấu hình cơ bản là quyết định endpoint nào cần bảo vệ
        // và einpoint nào ai cũng có thể truy cập
        httpSecurity.authorizeHttpRequests(request ->
                //cho phép endpoint-method POST này dc truy cập public
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
//                        .requestMatchers(HttpMethod.GET,"/users").hasAuthority("SCOPE_ADMIN")
//                        .requestMatchers(HttpMethod.GET,"/users").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated());

        //phần 2
        //cấu hình khi 1 user đã bỏ token vào header r thì sẽ sử dụng được các endpoint mong muốn
//        httpSecurity.oauth2ResourceServer(oauth2 ->
//                //decoder() là 1 interface > cần 1 impl của interface này
//                //ở Bean JwtDecoder bên dưới
//                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
//                );

        //phần này để sửa lại SCOPE_admin thành ROLE_admin
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        //khi authenication fail sẽ điều hướng user đi đến chổ mình muốn/ hoặc return error message
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        //phần 3
        //mặc định sẽ bật cấu hình csrf
        // bảo vệ endpoint khỏi attack crosstype
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    //spring security config cung cấp chức năng config dc điểm mà error nó sẽ xảy ra
    //lỗi 401 không thể bắt ở handleException class nên bắt ở đây

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return  jwtAuthenticationConverter;
    }
//    @Bean
//    JwtDecoder jwtDecoder(){
//        //nimbus dùng để mã hoá token
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
