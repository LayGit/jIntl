package com.laylib.jintl.monitor.listener;

import com.laylib.jintl.entity.SourceProperties;

import java.util.List;

/**
 * Source Changed Listener
 *
 * @author Lay
 */
public interface SourceChangedListener {
    /**
     * on source changed
     * @param sourceProperties changed sources
     */
    void onChange(List<SourceProperties> sourceProperties);
}
