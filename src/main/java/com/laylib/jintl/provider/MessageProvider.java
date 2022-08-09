package com.laylib.jintl.provider;

import java.util.Locale;

/**
 * Message Provider
 *
 * @author Lay
 */
public interface MessageProvider {
    String getMessage(String code, Locale locale);
    String getMessage(String code, Object[] args, Locale locale);
}
