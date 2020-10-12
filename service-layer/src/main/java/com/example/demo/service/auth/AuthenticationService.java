package com.example.demo.service.auth;

import com.example.demo.service.dto.JwtLogInDetails;

import java.util.Date;

public interface AuthenticationService {

    JwtLogInDetails authenticateUser(String username, String password);

    void registerUser(String username, String email, String password, String passwordConfirmation, String firstName,
                        String lastName, Date registrationDate);

    void updatePassword(String oldPassword, String newPassword, String newPasswordConfirmation);

}
