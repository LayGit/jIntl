package com.laylib.jintl.loader;

import com.laylib.jintl.entity.SourceIndex;
import com.laylib.jintl.entity.SourceProperties;

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
     * @return
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
     * @return
     */
    String getSourcePath(String tag, Locale locale);
    
    void startMonitor();

    void stopMonitor();
}
