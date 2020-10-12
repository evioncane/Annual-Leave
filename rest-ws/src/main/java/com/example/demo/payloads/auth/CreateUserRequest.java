package com.example.demo.payloads.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "email cannot be empty!")
    @Size(max = 50)
    @Email(message = "Please enter a valid email!")
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private Date registeredDate;

    @NotEmpty
    @Size(min = 1)
    private Set<String> roles;

}
