package com.laylib.jintl.exception;

import java.util.Locale;

/**
 * Exception thrown when a message can't be resolved.
 *
 * @author Lay
 */
public class NoSuchMessageException extends RuntimeException {
    /**
     * Create a new exception.
     * @param code the code that could not be resolved for given locale
     * @param locale the locale that was used to search for the code within
     */
    public NoSuchMessageException(String code, Locale locale) {
        super("No message found under code '" + code + "' for locale '" + locale + "'.");
    }

    /**
     * Create a new exception.
     * @param code the code that could not be resolved for given locale
     */
    public NoSuchMessageException(String code) {
        super("No message found under code '" + code + "' for locale '" + Locale.getDefault() + "'.");
    }
}
