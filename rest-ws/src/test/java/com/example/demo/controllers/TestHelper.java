package com.example.demo.controllers;

import com.example.demo.exceptions.auth.LogInException;
import com.example.demo.payloads.auth.LoginRequest;
import com.example.demo.payloads.auth.UpdatePasswordRequest;
import com.example.demo.service.dto.JwtLogInDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

public class TestHelper {

    protected String baseAuthUrl;

    protected String logInUrl;

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
        headers.add("User-Agent", "Spring's RestTemplate" );  // value can be whatever
        headers.add("Authorization", "Bearer "+token);

        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword(oldPassword);
        updatePasswordRequest.setNewPassword(newPassword);
        updatePasswordRequest.setNewPasswordConfirmation(newPasswordConfirmation);

        HttpEntity<UpdatePasswordRequest> request = new HttpEntity<>(updatePasswordRequest, headers);
        ResponseEntity<String> response = this.restTemplate.postForEntity(uri, request, String.class);
        return response.getBody();

    }
}
