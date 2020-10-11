package com.example.demo.payloads.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String username;
    private String email;
    private String password;
    private String passwordConfirmation;
    private String firstName;
    private String lastName;

}
