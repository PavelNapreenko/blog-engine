package ru.pnapreenko.blogengine.api.components;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.pnapreenko.blogengine.api.interfaces.MailSenderProviderInterface;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.api.utils.MessageCreator;

import javax.mail.MessagingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "prod", matchIfMissing = true)
@RequiredArgsConstructor
public class MailServiceProviderProd implements MailSenderProviderInterface {
    private final MessageCreator mc;

    @Override
    public void sendMail(String recipientEmail, String code) throws MessagingException, UnknownHostException {
        final String hostName = InetAddress.getLocalHost().getCanonicalHostName();
        final String url = String.format(ConfigStrings.AUTH_SERVER_URL_PROD, hostName);
        mc.sendMessage(recipientEmail, code, url);
    }
}
