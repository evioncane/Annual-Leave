package com.example.demo.payloads.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username is empty!")
    private String username;

    @NotBlank(message = "Password is empty!")
    @Size(min = 3, max = 40)
    private String password;
}
