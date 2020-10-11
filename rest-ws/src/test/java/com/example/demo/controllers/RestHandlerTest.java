package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class RestHandlerTest {

    @Test
    public void integrationTest() {
        //assertEquals(2, nr);
    }

}
