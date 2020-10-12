package com.example.demo.payloads.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateUserResponse {

    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public CreateUserResponse(CreateUserRequest request) {
        this.username = request.getUsername();
        this.email = request.getEmail();
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
    }
}
