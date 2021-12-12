package ru.pnapreenko.blogengine.api.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Data
@RequiredArgsConstructor
public class MessageCreator {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendMessage(String recipientEmail, String code, String url) throws MessagingException {
        final String subject = ConfigStrings.AUTH_MAIL_SUBJECT;
        final String message = String.format(ConfigStrings.AUTH_MAIL_MESSAGE, url, code);
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.setFrom(emailFrom);
        mimeMessage.setRecipients(Message.RecipientType.TO, recipientEmail);
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(message, "text/html; charset=utf-8");
        mailSender.send(mimeMessage);
    }
}
