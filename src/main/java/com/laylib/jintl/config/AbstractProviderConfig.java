package com.laylib.jintl.config;

import lombok.Data;

/**
 * base provider config
 *
 * @author Lay
 * @since 1.0.0
 */
@Data
public abstract class AbstractProviderConfig {
    /**
     * is refresh index while config changed
     */
    private boolean isWatchIndex;

    /**
     * is refresh source while source changed
     */
    private boolean isWatchSource;

    public abstract String getType();

    public abstract String getProviderClass();
}
