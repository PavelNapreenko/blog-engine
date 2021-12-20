package ru.pnapreenko.blogengine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.interfaces.MailSenderProviderInterface;

import javax.mail.MessagingException;
import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class MailSendService {
    private final MailSenderProviderInterface mspi;

    public void send(String recipientEmail, String code) throws MessagingException, UnknownHostException {
        mspi.sendMail(recipientEmail, code);
    }
}
