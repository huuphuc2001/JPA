package com.example.JPA.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
//tạo bảng lưu token hết hạn
//quét và xoá token đã hết hạn để logout
public class LogoutToken {
    @Id
    String id;
    Date expiryTime;//thời gian hết hạn của token
}
