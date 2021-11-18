package ru.pnapreenko.blogengine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class MailSendService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;

    public MailSendService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String recipientEmail, String subject, String message) throws MessagingException {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        mimeMessage.setFrom(emailFrom);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(message, "text/html; charset=utf-8");
        mailSender.send(mimeMessage);
    }
}
