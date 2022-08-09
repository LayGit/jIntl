package com.laylib.jintl.util;

/**
 * String Utils
 *
 * @author Lay
 */
public class StringUtils {
    public static final String EMPTY_STRING = "";

    public static boolean isEmpty(String s) {
        return s == null || EMPTY_STRING.equals(s);
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }
}
