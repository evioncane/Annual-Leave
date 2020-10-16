package com.example.demo.service.dto;

import com.example.demo.model.RoleEntity;
import com.example.demo.model.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date startDate;
    private Set<String> roles;

    public User(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.email = userEntity.getEmail();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        this.startDate = userEntity.getStartDate();
        this.roles = userEntity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet());
    }
}
