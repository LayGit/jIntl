package com.laylib.jintl.loader;

import com.laylib.jintl.config.BaseProviderConfig;
import com.laylib.jintl.entity.SourceIndex;
import com.laylib.jintl.formatter.SourceNameFormatter;
import com.laylib.jintl.monitor.SourceMonitor;
import com.laylib.jintl.monitor.listener.IndexChangedListener;
import com.laylib.jintl.monitor.listener.SourceChangedListener;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Abstract implementation of the {@link SourceLoader} interface
 *
 * @author Lay
 */
public abstract class AbstractSourceLoader<T extends BaseProviderConfig> implements SourceLoader {

    protected final T config;

    protected SourceMonitor monitor;

    protected IndexChangedListener indexChangedListener;

    protected SourceChangedListener sourceChangedListener;

    protected final SourceNameFormatter sourceNameFormatter;

    public AbstractSourceLoader(T config) {
        this.config = config;
        this.sourceNameFormatter = config.getSourceNameFormatter();
    }

    public void withMonitor(IndexChangedListener indexChangedListener, SourceChangedListener sourceChangedListener) {
        this.indexChangedListener = indexChangedListener;
        this.sourceChangedListener = sourceChangedListener;
        this.monitor = this.createMonitor();
    }

    protected abstract SourceMonitor createMonitor();

    @SuppressWarnings("unchecked")
    protected SourceIndex resolveIndex(Map<String, List<String>> index) {
        SourceIndex sourceIndex = new SourceIndex();
        for (Map.Entry<String, List<String>> obj : index.entrySet()) {
            SourceIndex.IndexItem indexItem = new SourceIndex.IndexItem();
            indexItem.setTag(obj.getKey());
            for (String locale : obj.getValue()) {
                indexItem.getLocales().add(Locale.forLanguageTag(locale));
            }
            sourceIndex.getItems().add(indexItem);
        }
        return sourceIndex;
    }

    public void onSourceIndex(SourceIndex sourceIndex) {
        if (this.monitor != null && sourceIndex != null) {
            this.monitor.watchSourcesWithIndex(sourceIndex);
        }
    }

    @Override
    public void startMonitor() {
        if (this.monitor != null) {
            if (config.getIndexWatchInterval() != null && config.getIndexWatchInterval() > 0) {
                this.monitor.startIndexMonitor();
            }

            if (config.getSourceWatchInterval() != null && config.getSourceWatchInterval() > 0) {
                this.monitor.startSourceMonitor();
            }
        }
    }

    @Override
    public void stopMonitor() {
        if (this.monitor != null) {
            this.monitor.stopIndexMonitor();
            this.monitor.stopSourceMonitor();
        }
    }
}
