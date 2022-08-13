package com.laylib.jintl.config;

import com.laylib.jintl.annotation.ProviderType;
import com.laylib.jintl.loader.LocalSourceLoader;
import com.laylib.jintl.loader.SourceLoader;
import com.laylib.jintl.provider.DefaultMessageProvider;
import com.laylib.jintl.provider.MessageProvider;

/**
 * Local Message Provider Config
 *
 * @author Lay
 */
@ProviderType("local")
public class LocalProviderConfig extends BaseProviderConfig {

    private static final Long DEFAULT_WATCH_INTERVAL = 1000L;

    @Override
    public Class<? extends MessageProvider> getProviderClass() {
        return DefaultMessageProvider.class;
    }

    @Override
    public Class<? extends SourceLoader> getLoaderClass() {
        return LocalSourceLoader.class;
    }

    /**
     * index watch interval
     */
    protected Long indexWatchInterval;

    /**
     * source watch interval
     */
    protected Long sourceWatchInterval;

    public Long getIndexWatchInterval() {
        if (indexWatchInterval == null) {
            return DEFAULT_WATCH_INTERVAL;
        }
        return indexWatchInterval;
    }

    public void setIndexWatchInterval(Long indexWatchInterval) {
        this.indexWatchInterval = indexWatchInterval;
    }

    public Long getSourceWatchInterval() {
        if (sourceWatchInterval == null) {
            return DEFAULT_WATCH_INTERVAL;
        }
        return sourceWatchInterval;
    }

    public void setSourceWatchInterval(Long sourceWatchInterval) {
        this.sourceWatchInterval = sourceWatchInterval;
    }
}
