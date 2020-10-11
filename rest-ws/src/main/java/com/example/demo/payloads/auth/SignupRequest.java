package com.example.demo.payloads.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "email cannot be empty!")
    @Size(max = 50)
    @Email(message = "Please enter a valid email!")
    private String email;

    @NotBlank
    @Size(min = 3, max = 40)
    private String password;

    @NotBlank
    @Size(min = 3, max = 40)
    private String passwordConfirmation;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

}
