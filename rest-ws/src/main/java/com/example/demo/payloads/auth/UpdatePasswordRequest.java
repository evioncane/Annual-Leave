package com.example.demo.payloads.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdatePasswordRequest {

    @NotBlank
    @Size(min = 3, max = 40)
    private String oldPassword;

    @NotBlank
    @Size(min = 3, max = 40)
    private String newPassword;

    @NotBlank
    @Size(min = 3, max = 40)
    private String newPasswordConfirmation;
}
