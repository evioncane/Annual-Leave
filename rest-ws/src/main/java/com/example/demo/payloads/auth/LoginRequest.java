package com.example.demo.payloads.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username is empty!")
    private String username;

    @NotBlank(message = "Password is empty!")
    private String password;
}
