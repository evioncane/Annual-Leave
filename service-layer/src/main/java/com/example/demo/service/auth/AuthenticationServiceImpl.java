package com.example.demo.service.auth;

import com.example.demo.exceptions.auth.DeleteException;
import com.example.demo.exceptions.auth.LogInException;
import com.example.demo.exceptions.auth.PasswordUpdateException;
import com.example.demo.exceptions.auth.RegistrationException;
import com.example.demo.exceptions.auth.UpdateException;
import com.example.demo.model.RoleEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.model.UserHistoryEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserHistoryRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.util.Constants.DEFAULT_PASSWORD;
import static com.example.demo.util.Constants.EMAIL_IN_USE;
import static com.example.demo.util.Constants.NO_VALID_ROLES_FOUND;
import static com.example.demo.util.Constants.OLD_PASSWORDS_DO_NOT_MATCH;
import static com.example.demo.util.Constants.PASSWORDS_DO_NOT_MATCH;
import static com.example.demo.util.Constants.USERNAME_NOT_FOUND;
import static com.example.demo.util.Constants.USERNAME_TAKEN;
import static com.example.demo.util.Constants.WRONG_CREDENTIALS;
import static java.lang.String.format;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final UserHistoryRepository userHistoryRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                                     UserHistoryRepository userHistoryRepository, RoleRepository roleRepository,
                                     PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userHistoryRepository = userHistoryRepository;
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
    public void registerUser(String username, String email, String firstName, String lastName, Date registrationDate,
                             Set<String> roles) {
        if (this.userRepository.existsByUsername(username)) {
            throw new RegistrationException(USERNAME_TAKEN);
        }
        if (this.userRepository.existsByEmail(email)) {
            throw new RegistrationException(EMAIL_IN_USE);
        }

        Set<RoleEntity> roleEntitySet = this.roleRepository.findByNameIn(roles);
        if (roleEntitySet.isEmpty()) {
            throw new RegistrationException(NO_VALID_ROLES_FOUND);
        }

        // If date is empty then a new date (for now) is created
        registrationDate = registrationDate != null ? registrationDate : new Date();

        // Create new user's account
        UserEntity user = new UserEntity(username, email, this.encoder.encode(DEFAULT_PASSWORD), firstName, lastName,
                registrationDate, roleEntitySet);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(String username, String email, String firstName, String lastName, Date registrationDate,
                           Set<String> roles) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User %s not found!", username)));
        if (email != null) {
            userEntity.setEmail(email);
        }
        if (firstName != null) {
            userEntity.setFirstName(firstName);
        }
        if (lastName != null) {
            userEntity.setLastName(lastName);
        }
        if (registrationDate != null) {
            userEntity.setStartDate(registrationDate);
        }
        if (roles != null && !roles.isEmpty()) {
            Set<RoleEntity> roleEntitySet = this.roleRepository.findByNameIn(roles);
            userEntity.setRoles(roleEntitySet);
        }
        this.userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        UserEntity userEntity= this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("User %s not found!", username)));
        this.userRepository.delete(userEntity);
        UserHistoryEntity userHistoryEntity = new UserHistoryEntity(new Date(), userEntity);
        this.userHistoryRepository.save(userHistoryEntity);
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
