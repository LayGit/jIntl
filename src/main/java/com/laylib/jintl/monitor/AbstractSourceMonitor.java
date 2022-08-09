package com.laylib.jintl.monitor;

import com.laylib.jintl.config.BaseProviderConfig;

/**
 * Abstract implementation of the {@link SourceMonitor} interface
 *
 * @author Lay
 */
public abstract class AbstractSourceMonitor<T extends BaseProviderConfig> implements SourceMonitor {

    protected final T config;

    public AbstractSourceMonitor(T config) {
        this.config = config;
    }
}
