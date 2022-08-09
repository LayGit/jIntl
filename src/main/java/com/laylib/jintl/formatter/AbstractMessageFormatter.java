package com.laylib.jintl.formatter;

import java.util.Locale;

/**
 * Abstract implementation of the {@link MessageFormatter} interface
 *
 * @author Lay
 */
public abstract class AbstractMessageFormatter implements MessageFormatter {

    private String code;
    private Locale locale;

    public AbstractMessageFormatter(String code, Locale locale) {
        this.code = code;
        this.locale = locale;
    }
}
