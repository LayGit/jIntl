package com.laylib.jintl.formatter;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Default Message Formatter
 *
 * @author Lay
 */
public class DefaultMessageFormatter extends AbstractMessageFormatter {

    private final MessageFormat messageFormat;

    public DefaultMessageFormatter(String msg, Locale locale) {
        super(msg, locale);
        this.messageFormat = new MessageFormat(msg, locale);
    }

    @Override
    public String format(Object[] args) {
        return messageFormat.format(args);
    }
}
