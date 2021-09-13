package ru.pnapreenko.blogengine.api.responses;

import java.util.HashMap;
import java.util.Map;

public class APIResponse {

    private static APIResponse.Builder getResult(boolean result) {
        return new APIResponse.Builder(result);
    }

    public static Map<String, Object> ok() {
        return getResult(true).build();
    }

    public static Map<String, Object> error() {
        return getResult(false).build();
    }

    public static Map<String, Object> ok(String key, Object payload) {
        return getResult(true).addPayload(key, payload).build();
    }


    private static class Builder {
        private final Map<String, Object> payload = new HashMap<>();

        public Builder(boolean result) {
            payload.put("result", result);
        }

        public Map<String, Object> build() {
            return payload;
        }

        public Builder addPayload(String key, Object payload) {
            this.payload.put(key, payload);
            return this;
        }
    }
}

