package com.rdevelop.hackernews.application.util;

import java.util.stream.Stream;

public final class TextUtils {
    private TextUtils() {
    }

    public static int countWords(String text) {
        return (int) Stream.of(text.split("\\s+"))
                .filter(tok -> tok.matches(".*[A-Za-z0-9].*"))
                .count();
    }
}
