package com.example.JPA.dto.respone;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//JWT
public class AuthenicationRespone {
    String token;
    boolean authenicated;//true/false kiểm tra xem user login thành công ko
}
