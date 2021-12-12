package ru.pnapreenko.blogengine.api.interfaces;

import javax.mail.MessagingException;

public interface MailSenderProviderInterface {
    void sendMail(String recipientEmail, String code) throws MessagingException;
}
