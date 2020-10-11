package com.example.demo.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class JwtLogInDetails {

    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private Set<String> roles;

    public JwtLogInDetails(String token, String username, String email, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
