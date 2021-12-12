package ru.pnapreenko.blogengine.services;

import org.springframework.stereotype.Service;
import ru.pnapreenko.blogengine.api.interfaces.MailSenderProviderInterface;

import javax.mail.MessagingException;
import java.net.UnknownHostException;

@Service
public class MailSendService {
    private final MailSenderProviderInterface mspi;

    public MailSendService(MailSenderProviderInterface mdi) {
        this.mspi = mdi;
    }

    public void send(String recipientEmail, String code) throws MessagingException, UnknownHostException {
        mspi.sendMail(recipientEmail, code);
    }
}
