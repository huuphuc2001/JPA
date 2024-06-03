package com.example.JPA.dto.respone;

import com.example.JPA.entity.Role;
import jakarta.persistence.ElementCollection;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRespone {
    String id;
    String username;
    String firstname;
    String lastname;
    LocalDate dob;
    //set đảm bảo các phần tử trong mảng là unit(duy nhất)
    Set<RoleResponse> roles;

}
