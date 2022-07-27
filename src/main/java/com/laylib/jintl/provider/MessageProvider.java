package com.laylib.jintl.provider;

import java.util.Locale;

/**
 * jIntl source provider
 *
 * @author Lay
 * @since 1.0.0
 */
public interface MessageProvider {
    /**
     * get message
     * @param code
     * @param locale
     * @return
     */
    String getMessage(String code, Locale locale);
}
