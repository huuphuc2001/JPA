package com.example.JPA.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
//No: constructer ko tham số
@NoArgsConstructor
//All: constructer đầy đủ tham số
@AllArgsConstructor
@Builder
//Fiel: nếu không ghi rõ thì mặc địch các biến là private
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogoutRequest {
    String token;
}
