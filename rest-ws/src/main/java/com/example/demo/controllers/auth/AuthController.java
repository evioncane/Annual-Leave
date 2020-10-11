package com.example.demo.controllers.auth;

import com.example.demo.exceptions.LogInException;
import com.example.demo.exceptions.RegistrationException;
import com.example.demo.payloads.auth.LoginRequest;
import com.example.demo.payloads.auth.SignUpResponse;
import com.example.demo.payloads.auth.SignupRequest;
import com.example.demo.service.auth.AuthenticationService;
import com.example.demo.service.dto.JwtLogInDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            JwtLogInDetails logInDetails = this.authenticationService.authenticateUser(loginRequest.getUsername(),
                    loginRequest.getPassword());
            return ResponseEntity.ok(logInDetails);
        } catch (LogInException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> authenticateUser(@RequestBody SignupRequest signupRequest) {
        try {
            this.authenticationService.registerUser(signupRequest.getUsername(), signupRequest.getEmail(),
                    signupRequest.getPassword(), signupRequest.getPasswordConfirmation(), signupRequest.getFirstName(),
                    signupRequest.getLastName());
            return ResponseEntity.ok(new SignUpResponse(signupRequest));
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
