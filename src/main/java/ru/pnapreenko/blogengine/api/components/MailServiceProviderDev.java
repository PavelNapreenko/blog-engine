package ru.pnapreenko.blogengine.api.components;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.pnapreenko.blogengine.api.interfaces.MailSenderProviderInterface;
import ru.pnapreenko.blogengine.api.utils.MessageCreator;
import ru.pnapreenko.blogengine.config.ConfigStrings;

import javax.mail.MessagingException;
import java.net.InetAddress;

@Component
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "dev")
@RequiredArgsConstructor
public class MailServiceProviderDev implements MailSenderProviderInterface {
    private final Environment environment;
    private final MessageCreator mc;

    @Override
    public void sendMail(String recipientEmail, String code) throws MessagingException {
        final String port = environment.getProperty("server.port");
        final String hostName = InetAddress.getLoopbackAddress().getHostName();
        final String url = String.format(ConfigStrings.AUTH_SERVER_URL.getName(), hostName, port);
        mc.sendMessage(recipientEmail, code, url);
    }
}
