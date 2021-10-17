package ru.pnapreenko.blogengine.api.utils;

import com.github.cage.Cage;
import com.github.cage.GCage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaUtils {

    public static String getRandomCode(final int codeLength) {
        final int LEFT = 48;
        final int RIGHT = 122;

        return new Random().ints(LEFT, RIGHT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(codeLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String getImageBase64(String code) throws IOException {
        Cage cage = new GCage();
        String base64EncodedImage;
        String name = "data:image/png;base64, ";
        base64EncodedImage = name +
                Base64.getEncoder().encodeToString(generate(cage, code));
        return base64EncodedImage;
    }

    public static byte[] generate(Cage cage, String text) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (os) {
            cage.draw(
                    text != null ? text : cage.getTokenGenerator().next(),
                    os);
        }
        return os.toByteArray();
    }
}
