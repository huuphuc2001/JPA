package com.example.JPA.dto.respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//những fiel nào null thì ko hiển thị
//<T> là một kiểu tham số chung, cho phép lớp này có thể làm việc với bất kỳ kiểu dữ liệu nào
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRespone <T>{
     int code = 1000;
     String message;
     T result;

}
