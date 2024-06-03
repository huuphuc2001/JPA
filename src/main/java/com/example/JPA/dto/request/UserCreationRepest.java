package com.example.JPA.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;

//validation ở đây luôn = vd: yêu cầu người dùng nhập ít nhất 8 ký tự
@Data
//No: constructer ko tham số
@NoArgsConstructor
//All: constructer đầy đủ tham số
@AllArgsConstructor
@Builder
//Fiel: nếu không ghi rõ thì mặc địch các biến là private
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRepest {
    @Size(min = 3, message = "USER_INVALID")
    String username;
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String firstname;
    String lastname;
    LocalDate dob;


}
