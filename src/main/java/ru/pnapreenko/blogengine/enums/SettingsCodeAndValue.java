package ru.pnapreenko.blogengine.enums;

public class SettingsCodeAndValue {

        public enum Code {
            MULTIUSER_MODE("Многопользовательский режим"),
            POST_PREMODERATION("Премодерация постов"),
            STATISTICS_IS_PUBLIC("Показывать всем статистику блога");

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

