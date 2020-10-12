package com.example.demo.controllers.auth;

import com.example.demo.controllers.TestHelper;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.dto.JwtLogInDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Optional;

import static com.example.demo.util.Constants.PASSWORDS_UPDATED;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest extends TestHelper {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        this.baseAuthUrl = "http://localhost:" + randomServerPort+"/auth";
        this.logInUrl = this.baseAuthUrl + "/signin";
        this.updatePasswordUrl = this.baseAuthUrl + "/update/password";
        this.restTemplate = new RestTemplate();
        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsername("user");
        UserEntity userEntity = optionalUserEntity.orElseThrow(() -> new UsernameNotFoundException("User: user not found!"));
        userEntity.setPassword(this.encoder.encode("test"));
        this.userRepository.save(userEntity);
    }

    @Test
    public void iAmAbleToLogIn() throws URISyntaxException, JsonProcessingException {
        JwtLogInDetails jwtLogInDetails = logIn("user", "test");
        assertEquals(jwtLogInDetails.getUsername(), "user");
        assertNotNull(jwtLogInDetails.getToken());
    }

    @Test
    public void logInFailsWithWrongCredentials() {
        assertThrows(HttpClientErrorException.Unauthorized.class, () -> logIn("user", "test2"));
    }

    @Test
    public void canUpdateMyPassword() throws URISyntaxException, JsonProcessingException {
        JwtLogInDetails jwtLogInDetails = logIn("user", "test");
        String jwt = jwtLogInDetails.getToken();
        String message = updatePassword("test", "test2", "test2", jwt);
        assertEquals(PASSWORDS_UPDATED, message);
    }

}