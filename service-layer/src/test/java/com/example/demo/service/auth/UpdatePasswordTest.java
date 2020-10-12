package com.example.demo.service.auth;

import com.example.demo.exceptions.auth.PasswordUpdateException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserHistoryRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UpdatePasswordTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserHistoryRepository userHistoryRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthenticationService authenticationService = new AuthenticationServiceImpl(authenticationManager,
            userRepository, userHistoryRepository, roleRepository, encoder, jwtUtils);

    @Test(expected= PasswordUpdateException.class)
    public void updatePasswordWhenNotAuthenticated() {
        this.authenticationService.updatePassword("oldPass", "newPass", "newPass");
    }
}
