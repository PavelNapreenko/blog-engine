package ru.pnapreenko.blogengine.api.interfaces;

import javax.mail.MessagingException;
import java.net.UnknownHostException;

public interface MailSenderProviderInterface {
    void sendMail(String recipientEmail, String code) throws MessagingException, UnknownHostException;
}
