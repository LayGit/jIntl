package com.laylib.jintl.config;

import lombok.Data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * jIntl configuration
 *
 * @author Lay
 * @since 1.0.0
 */
@Data
public class IntlConfig {

    /**
     * is return code when message is null
     */
    private boolean useCodeAsDefaultMessage;

    /**
     * is fallback to Locale without country
     */
    private boolean fallbackLanguageOny;

    /**
     * config for provider
     */
    private AbstractProviderConfig providerConfig;

    /**
     * source charset
     */
    private Charset charset = StandardCharsets.UTF_8;
}
