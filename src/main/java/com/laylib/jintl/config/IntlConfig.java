package com.laylib.jintl.config;

/**
 * jIntl Configuration
 *
 * @author Lay
 */
public class IntlConfig {
    /**
     * is return code when message is null
     */
    private boolean useCodeAsDefaultMessage;

    /**
     * is fallback to Locale without country
     */
    private boolean fallbackLanguageOnly;

    public boolean isUseCodeAsDefaultMessage() {
        return useCodeAsDefaultMessage;
    }

    public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
        this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
    }

    public boolean isFallbackLanguageOnly() {
        return fallbackLanguageOnly;
    }

    public void setFallbackLanguageOnly(boolean fallbackLanguageOnly) {
        this.fallbackLanguageOnly = fallbackLanguageOnly;
    }
}
