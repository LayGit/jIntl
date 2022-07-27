package com.laylib.jintl.provider;

import java.util.List;
import java.util.Map;

/**
 * config listener interface
 *
 * @author Lay
 * @since 1.0.0
 */
public interface IndexListener {
    void onChanged(Map<String, List<String>> config);
}
