package com.example.JPA.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRepest {

    String password;
    String firstname;
    String lastname;
    LocalDate dob;
    List<String> roles;

}
