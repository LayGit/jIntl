package com.laylib.jintl.monitor.listener;

import com.laylib.jintl.entity.SourceIndex;

/**
 * Index Changed Listener
 *
 * @author Lay
 */
public interface IndexChangedListener {
    /**
     * on index changed
     * @param sourceIndex changed index
     */
    void onChange(SourceIndex sourceIndex);
}
