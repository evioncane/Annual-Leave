package com.example.demo.handlers;

import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("rest/v1")
public class RestHandler {

    @Autowired
    private TestService testService;

    @GetMapping("/greeting")
    public String greeting() {
        return "Greetings!";
    }
}
