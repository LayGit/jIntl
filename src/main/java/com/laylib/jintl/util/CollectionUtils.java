package com.laylib.jintl.util;

import java.util.Collection;

/**
 * Collection Utils
 *
 * @author Lay
 */
public class CollectionUtils {

    /**
     * is Object array empty
     * @param arr Object array
     * @return is empty
     */
    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(Object[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}
