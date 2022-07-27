package com.laylib.jintl;

import com.laylib.jintl.config.IntlConfig;
import com.laylib.jintl.provider.MessageProvider;
import com.laylib.jintl.provider.SourceProviderFactory;
import org.apache.commons.lang3.ObjectUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * jIntl main class
 *
 * @author Lay
 * @since 1.0.0
 */
public class IntlSource {
    private final IntlConfig config;

    /**
     * Cache to hold already generated MessageFormats per message.
     * Used for passed-in default messages. MessageFormats for resolved
     * codes are cached on a specific basis in subclasses.
     */
    private final Map<String, Map<Locale, MessageFormat>> messageFormatsPerMessage = new HashMap<>();

    private static final MessageFormat INVALID_MESSAGE_FORMAT = new MessageFormat("");

    /**
     * source provider
     */
    private final MessageProvider sourceProvider;

    public IntlSource(IntlConfig config) {
        this.config = config;
        this.sourceProvider = SourceProviderFactory.build(config);
    }

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        String message = sourceProvider.getMessage(code, locale);
        if (message == null) {
            if (defaultMessage != null) {
                message = defaultMessage;
            }

            if (message == null && config.isUseCodeAsDefaultMessage()) {
                message = code;
            }
        }

        if (message != null && ObjectUtils.isNotEmpty(args)) {
            // format
            MessageFormat messageFormat = new MessageFormat(message, locale);
            message = messageFormat.format(args);
        }

        return message;
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        return getMessage(code, args, null, locale);
    }

    public String getMessage(String code, String defaultMessage, Locale locale) {
        return getMessage(code, null, defaultMessage, locale);
    }

    public String getMessage(String code, Locale locale) {
        return getMessage(code, null, null, locale);
    }
}
