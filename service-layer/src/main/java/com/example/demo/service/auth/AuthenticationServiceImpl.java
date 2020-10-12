package com.example.demo.service.auth;

import com.example.demo.exceptions.auth.LogInException;
import com.example.demo.exceptions.auth.PasswordUpdateException;
import com.example.demo.exceptions.auth.RegistrationException;
import com.example.demo.model.RoleEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.user.UserDetailsImpl;
import com.example.demo.service.dto.JwtLogInDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.util.Constants.EMAIL_IN_USE;
import static com.example.demo.util.Constants.OLD_PASSWORDS_DO_NOT_MATCH;
import static com.example.demo.util.Constants.PASSWORDS_DO_NOT_MATCH;
import static com.example.demo.util.Constants.USERNAME_TAKEN;
import static com.example.demo.util.Constants.WRONG_CREDENTIALS;
import static java.lang.String.format;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                                     RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtLogInDetails authenticateUser(String username, String password) {
        try {
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = this.jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            return new JwtLogInDetails(jwt, userDetails.getUsername(), userDetails.getEmail(), roles);
        } catch (AuthenticationException e) {
            logger.error("User({}) tried to log in but failed!", username);
            throw new LogInException(WRONG_CREDENTIALS);
        }
    }

    @Override
    public void registerUser(String username, String email, String password, String passwordConfirmation,
                               String firstName, String lastName) {
        if (!password.equals(passwordConfirmation)) {
            throw new RegistrationException(PASSWORDS_DO_NOT_MATCH);
        }
        if (this.userRepository.existsByUsername(username)) {
            throw new RegistrationException(USERNAME_TAKEN);
        }
        if (this.userRepository.existsByEmail(email)) {
            throw new RegistrationException(EMAIL_IN_USE);
        }

        Optional<RoleEntity> roleOptional = this.roleRepository.findByName("user");
        RoleEntity userRole;
        userRole = roleOptional.orElseGet(() -> new RoleEntity("user"));

        // Create new user's account
        UserEntity user = new UserEntity(username, email, this.encoder.encode(password), firstName, lastName, Set.of(userRole));
        this.userRepository.save(user);
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword, String newPasswordConfirmation) {
        if (!newPassword.equals(newPasswordConfirmation)) {
            throw new PasswordUpdateException(PASSWORDS_DO_NOT_MATCH);
        }
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserEntity userEntity = this.userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new UsernameNotFoundException(format("User %s not found!", userDetails.getId())));
            if (!this.encoder.matches(oldPassword, userEntity.getPassword())) {
                throw new PasswordUpdateException(OLD_PASSWORDS_DO_NOT_MATCH);
            }
            String encodedNewPassword = this.encoder.encode(newPassword);
            userEntity.setPassword(encodedNewPassword);
            this.userRepository.save(userEntity);
        } catch (NullPointerException e) {
            throw new PasswordUpdateException("User not authenticated!");
        }
    }
}
