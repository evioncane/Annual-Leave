package com.example.demo.controllers.auth;

import com.example.demo.exceptions.auth.LogInException;
import com.example.demo.exceptions.auth.PasswordUpdateException;
import com.example.demo.exceptions.auth.RegistrationException;
import com.example.demo.payloads.auth.LoginRequest;
import com.example.demo.payloads.auth.SignUpResponse;
import com.example.demo.payloads.auth.SignupRequest;
import com.example.demo.payloads.auth.UpdatePasswordRequest;
import com.example.demo.service.auth.AuthenticationService;
import com.example.demo.service.dto.JwtLogInDetails;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.util.Constants.PASSWORDS_UPDATED;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtLogInDetails logInDetails = this.authenticationService.authenticateUser(loginRequest.getUsername(),
                    loginRequest.getPassword());
            return ResponseEntity.ok(logInDetails);
        } catch (LogInException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            this.authenticationService.registerUser(signupRequest.getUsername(), signupRequest.getEmail(),
                    signupRequest.getPassword(), signupRequest.getPasswordConfirmation(), signupRequest.getFirstName(),
                    signupRequest.getLastName());
            return ResponseEntity.ok(new SignUpResponse(signupRequest));
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/update/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        try {
            this.authenticationService.updatePassword(updatePasswordRequest.getOldPassword(),
                    updatePasswordRequest.getNewPassword(), updatePasswordRequest.getNewPasswordConfirmation());
            return ResponseEntity.ok().body(PASSWORDS_UPDATED);
        } catch (PasswordUpdateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleError(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
