package com.example.JPA.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.Id;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//để đánh dấu class này là 1 table dùng @Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role {
    @Id
    String name;
    String description;
    @ManyToMany
    Set<Permission> permissions;
}
