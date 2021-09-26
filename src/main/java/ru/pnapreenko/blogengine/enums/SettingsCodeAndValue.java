package ru.pnapreenko.blogengine.enums;

import ru.pnapreenko.blogengine.config.ConfigStrings;

public class SettingsCodeAndValue {

        public enum Code {
            MULTIUSER_MODE(ConfigStrings.MULTIUSER_MODE),
            POST_PREMODERATION(ConfigStrings.POST_PREMODERATION),
            STATISTICS_IS_PUBLIC(ConfigStrings.STATISTICS_IS_PUBLIC);

            private final String name;

            Code(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        public enum Value {
            YES("Да", true),
            NO("Нет", false);

            private final String name;
            private final boolean value;

            Value(String name, boolean value) {
                this.name = name;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public boolean getValue(){
                return value;
            }
        }
    }

