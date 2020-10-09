package com.example.demo.handlers;

import com.example.demo.service.TestService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RestHandlerTest {

    @Autowired
    TestService testService;

    @Test
    public void integrationTest() {
        testService.greet();
        int nr = testService.beRude();
        assertEquals(2, nr);
    }

}