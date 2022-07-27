package com.laylib.jintl.provider;

import java.util.Properties;

/**
 * source listener
 *
 * @author Lay
 * @since 1.0.0
 */
public interface SourceListener {
    void onChanged(String tag, String locale, Properties properties);
}
