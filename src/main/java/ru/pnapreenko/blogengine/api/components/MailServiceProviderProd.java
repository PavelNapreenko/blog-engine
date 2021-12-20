package ru.pnapreenko.blogengine.api.components;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.pnapreenko.blogengine.api.interfaces.MailSenderProviderInterface;
import ru.pnapreenko.blogengine.api.utils.MessageCreator;

import javax.mail.MessagingException;
import java.net.UnknownHostException;

@Component
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "prod", matchIfMissing = true)
@RequiredArgsConstructor
public class MailServiceProviderProd implements MailSenderProviderInterface {
    private final MessageCreator mc;

    @Value("${blogapp.external-service}")
    private String externalServiceName;

    public String getExternalServiceName() {
        return externalServiceName;
    }

    @Override
    public void sendMail(String recipientEmail, String code) throws MessagingException, UnknownHostException {
        final String url = getExternalServiceName();
        mc.sendMessage(recipientEmail, code, url);
    }
}
