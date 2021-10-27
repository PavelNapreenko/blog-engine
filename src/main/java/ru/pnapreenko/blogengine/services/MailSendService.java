package ru.pnapreenko.blogengine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class MailSendService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public MailSendService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String recipientEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
