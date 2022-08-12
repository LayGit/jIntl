package com.laylib.jintl.loader;

import com.laylib.jintl.entity.SourceIndex;
import com.laylib.jintl.entity.SourceProperties;
import com.laylib.jintl.monitor.listener.IndexChangedListener;
import com.laylib.jintl.monitor.listener.SourceChangedListener;

import java.util.List;
import java.util.Locale;

/**
 * Index Loader
 *
 * @author Lay
 */
public interface SourceLoader {
    /**
     * load source index
     * @return source index
     */
    SourceIndex loadIndex();

    /**
     * load all sources
     * @param index source index
     * @return source list
     */
    List<SourceProperties> loadSources(SourceIndex index);

    /**
     * load source
     * @param tag       source tag
     * @param locale    source locale
     * @return source properties
     */
    SourceProperties loadSource(String tag, Locale locale);

    /**
     * get source path
     * @param tag       source tag
     * @param locale    locale
     * @return source path
     */
    String getSourcePath(String tag, Locale locale);

    /**
     * start monitor
     */
    void startMonitor();

    /**
     * stop monitor
     */
    void stopMonitor();

    /**
     * on source index loaded
     * @param sourceIndex index
     */
    void onSourceIndex(SourceIndex sourceIndex);

    /**
     * register monitor listeners
     * @param indexChangedListener  index changed listener
     * @param sourceChangedListener source changed listener
     */
    void withMonitor(IndexChangedListener indexChangedListener, SourceChangedListener sourceChangedListener);
}
