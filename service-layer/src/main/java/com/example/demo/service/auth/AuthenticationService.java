package com.example.demo.service.auth;

import com.example.demo.service.dto.JwtLogInDetails;
import com.example.demo.service.dto.User;

import java.util.Date;
import java.util.Set;

public interface AuthenticationService {

    JwtLogInDetails authenticateUser(String username, String password);

    void logOut();

    Set<User> getAllUsers();

    void registerUser(String username, String email, String firstName,
                      String lastName, Date registrationDate, Set<String> roles);

    void updateUser(String username, String email, String firstName, String lastName, Date registrationDate, Set<String> roles);

    void deleteUser(String username);

    void updatePassword(String oldPassword, String newPassword, String newPasswordConfirmation);

}
