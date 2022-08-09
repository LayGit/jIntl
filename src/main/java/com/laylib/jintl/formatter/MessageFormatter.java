package com.laylib.jintl.formatter;

/**
 * Message Formatter
 *
 * @author Lay
 */
public interface MessageFormatter {
    /**
     * format message
     * @param args      arguments
     * @return  formatted message
     */
    String format(Object[] args);
}
