package com.example.demo.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String SENDER_EMAIL = "annual.leave93@gmail.com";
    private static final String SENDER_PASSWORD = "notsafepassword";
    private static final String FROM = "noreply@annualLeave.com";



    @Override
    public void sendEmail(String subject, String text, String... receivers) {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost("smtp.gmail.com");
        emailSender.setPort(587);

        emailSender.setUsername(SENDER_EMAIL);
        emailSender.setPassword(SENDER_PASSWORD);

        Properties props = emailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(receivers);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
