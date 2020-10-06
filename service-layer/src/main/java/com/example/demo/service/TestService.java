package com.example.demo.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private static final Logger logger = LogManager.getLogger(TestService.class);

    public String greet() {
        logger.debug("Greetings in logs");
        return "Greetings!";
    }
}
