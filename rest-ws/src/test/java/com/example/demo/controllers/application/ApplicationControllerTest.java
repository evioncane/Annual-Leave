package com.example.demo.controllers.application;

import com.example.demo.controllers.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationControllerTest extends TestHelper {

    private String baseApplicationUrl;

    private String getApplicationsUrl;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    public void setUp() {
        this.restTemplate = new RestTemplate();
        this.baseApplicationUrl = "http://localhost:" + randomServerPort +"/application";
        this.getApplicationsUrl = this.baseApplicationUrl + "/all";
    }

}