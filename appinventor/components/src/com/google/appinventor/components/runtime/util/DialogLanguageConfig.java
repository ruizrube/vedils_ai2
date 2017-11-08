package com.google.appinventor.components.runtime.util;

public class DialogLanguageConfig {
    private final String languageCode;
    private final String accessToken;

    public DialogLanguageConfig(final String languageCode, final String accessToken) {
        this.languageCode = languageCode;
        this.accessToken = accessToken;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String toString() {
        return languageCode;
    }
}