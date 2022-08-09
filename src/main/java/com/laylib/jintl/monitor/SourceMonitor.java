package com.laylib.jintl.monitor;

import com.laylib.jintl.entity.SourceIndex;

/**
 * Source Monitor
 *
 * @author Lay
 */
public interface SourceMonitor {

    /**
     * start index monitor
     */
    void startIndexMonitor();

    /**
     * start source monitor
     */
    void startSourceMonitor();

    /**
     * stop index monitor
     */
    void stopIndexMonitor();

    /**
     * stop source monitor
     */
    void stopSourceMonitor();

    /**
     * watch sources with index
     * @param sourceIndex target index
     */
    void watchSourcesWithIndex(SourceIndex sourceIndex);
}
