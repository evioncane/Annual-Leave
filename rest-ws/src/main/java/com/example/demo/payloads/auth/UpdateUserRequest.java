package com.example.demo.payloads.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "Username cannot be empty!")
    @Size(min = 3, max = 20)
    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Date registeredDate;

    private Set<String> roles;

    public boolean allOtherFieldsAreEmpty() {
        return email == null && firstName == null && lastName == null && registeredDate == null && roles == null;
    }
}
