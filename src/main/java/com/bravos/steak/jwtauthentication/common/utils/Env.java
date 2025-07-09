package com.bravos.steak.jwtauthentication.common.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {

    private final static Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static String get(String key) {
        String value = System.getenv(key);
        return value == null ? dotenv.get(key) : value;
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : (dotenv.get(key) != null ? dotenv.get(key) : defaultValue);
    }

}
