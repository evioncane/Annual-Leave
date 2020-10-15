package com.example.demo.service.email;

public interface EmailService {
    void sendEmail(String subject, String message, String... receivers);
}
