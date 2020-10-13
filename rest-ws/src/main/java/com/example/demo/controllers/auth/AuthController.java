package com.example.demo.controllers.auth;

import com.example.demo.exceptions.auth.DeleteException;
import com.example.demo.exceptions.auth.LogInException;
import com.example.demo.exceptions.auth.PasswordUpdateException;
import com.example.demo.exceptions.auth.RegistrationException;
import com.example.demo.payloads.auth.LoginRequest;
import com.example.demo.payloads.auth.CreateUserResponse;
import com.example.demo.payloads.auth.CreateUserRequest;
import com.example.demo.payloads.auth.UpdatePasswordRequest;
import com.example.demo.payloads.auth.UpdateUserRequest;
import com.example.demo.service.auth.AuthenticationService;
import com.example.demo.service.dto.JwtLogInDetails;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.util.Constants.LOG_OUT_MESSAGE;
import static com.example.demo.util.Constants.PASSWORDS_UPDATED;
import static com.example.demo.util.Constants.SOMETHING_WENT_WRONG;
import static com.example.demo.util.Constants.WRONG_UPDATE_REQUEST;
import static java.lang.String.format;

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

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser() {
        this.authenticationService.logOut();
        return ResponseEntity.ok(LOG_OUT_MESSAGE);
    }


    @PostMapping("/create/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        try {
            this.authenticationService.registerUser(createUserRequest.getUsername(), createUserRequest.getEmail(),
                    createUserRequest.getFirstName(), createUserRequest.getLastName(),
                    createUserRequest.getRegisteredDate(), createUserRequest.getRoles());
            return ResponseEntity.ok(new CreateUserResponse(createUserRequest));
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/update/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            if (updateUserRequest.allOtherFieldsAreEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WRONG_UPDATE_REQUEST);
            }
            this.authenticationService.updateUser(updateUserRequest.getUsername(), updateUserRequest.getEmail(),
                    updateUserRequest.getFirstName(), updateUserRequest.getLastName(), updateUserRequest.getRegisteredDate(),
                    updateUserRequest.getRoles());
            return ResponseEntity.ok(updateUserRequest);
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            this.authenticationService.deleteUser(username);
            return ResponseEntity.ok(format("User %s deleted successfully", username));
        } catch (DeleteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SOMETHING_WENT_WRONG);
        }
    }

    @PostMapping("/update/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        try {
            this.authenticationService.updatePassword(updatePasswordRequest.getOldPassword(),
                    updatePasswordRequest.getNewPassword(), updatePasswordRequest.getNewPasswordConfirmation());
            return ResponseEntity.status(HttpStatus.CREATED).body(PASSWORDS_UPDATED);
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
