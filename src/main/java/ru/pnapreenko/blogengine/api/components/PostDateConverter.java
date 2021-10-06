package ru.pnapreenko.blogengine.api.components;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import ru.pnapreenko.blogengine.api.utils.DateUtils;
import ru.pnapreenko.blogengine.config.ConfigStrings;

import java.text.SimpleDateFormat;
import java.time.Instant;

@JsonComponent
public class PostDateConverter {
    private static final String DATE_FORMAT = ConfigStrings.NEW_POST_DATE_FORMAT;
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public static class Serialize extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator jsonGenerator, SerializerProvider provider) {
            try {
                if (value == null) {
                    jsonGenerator.writeNull();
                } else {
                    jsonGenerator.writeString(DateUtils.formatDate(value, DATE_FORMAT));
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static class Deserialize extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser jsonparser, DeserializationContext context) {
            try {
                String date = jsonparser.getText();
                return (date != null) ? Instant.ofEpochMilli(sdf.parse(date).getTime()) : null;
            } catch (Exception ignored) {
            }
            return null;
        }
    }
}

