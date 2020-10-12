package com.example.demo.controllers;

import com.example.demo.payloads.auth.CreateUserRequest;
import com.example.demo.payloads.auth.LoginRequest;
import com.example.demo.payloads.auth.UpdatePasswordRequest;
import com.example.demo.payloads.auth.UpdateUserRequest;
import com.example.demo.service.dto.JwtLogInDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestHelper {

    protected String baseAuthUrl;

    protected String logInUrl;

    protected String createUserUrl;

    protected String updateUserUrl;

    protected String deleteUserUrl;

    protected String updatePasswordUrl;

    protected RestTemplate restTemplate;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected JwtLogInDetails logIn(String username, String password) throws URISyntaxException, JsonProcessingException {
        URI uri = new URI(this.logInUrl);
        HttpHeaders headers = new HttpHeaders();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(uri, request, String.class);
        return this.objectMapper.readValue(response.getBody(), JwtLogInDetails.class);

    }

    protected String updatePassword(String oldPassword, String newPassword, String newPasswordConfirmation, String token)
            throws URISyntaxException {
        URI uri = new URI(this.updatePasswordUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "Spring's RestTemplate" );
        headers.add("Authorization", "Bearer "+token);

        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword(oldPassword);
        updatePasswordRequest.setNewPassword(newPassword);
        updatePasswordRequest.setNewPasswordConfirmation(newPasswordConfirmation);

        HttpEntity<UpdatePasswordRequest> request = new HttpEntity<>(updatePasswordRequest, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(uri, request, String.class);
        return response.getBody();

    }

    protected int createUser(String username, String email, String firstName, String lastName, Date registeredDate,
                             Set<String> roles, String token) throws URISyntaxException {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername(username);
        userRequest.setEmail(email);
        userRequest.setFirstName(firstName);
        userRequest.setLastName(lastName);
        userRequest.setRegisteredDate(registeredDate);
        userRequest.setRoles(roles);

        URI uri = new URI(this.createUserUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "Spring's RestTemplate" );
        headers.add("Authorization", "Bearer "+token);

        HttpEntity<CreateUserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(uri, request, String.class);
        return response.getStatusCodeValue();
    }

    protected int updateUser(String username, String email, String firstName, String lastName, Date registeredDate,
                             Set<String> roles, String token) throws URISyntaxException {
        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setUsername(username);
        userRequest.setEmail(email);
        userRequest.setFirstName(firstName);
        userRequest.setLastName(lastName);
        userRequest.setRegisteredDate(registeredDate);
        userRequest.setRoles(roles);

        URI uri = new URI(this.updateUserUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent", "Spring's RestTemplate" );
        headers.add("Authorization", "Bearer "+token);

        HttpEntity<UpdateUserRequest> request = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(uri, request, String.class);
        return response.getStatusCodeValue();
    }

    protected void deleteUser(String username, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Spring's RestTemplate" );
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Object> request = new HttpEntity<>(headers);
        this.restTemplate.exchange(this.deleteUserUrl + "/" + username, HttpMethod.DELETE, request, String.class);
    }
}
