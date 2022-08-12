package com.laylib.jintl.provider;

import com.laylib.jintl.config.BaseProviderConfig;
import com.laylib.jintl.loader.SourceLoader;

/**
 * Default Message Provider
 *
 * @author Lay
 */
public class DefaultMessageProvider extends AbstractMessageProvider<BaseProviderConfig> {
    public DefaultMessageProvider(BaseProviderConfig config, SourceLoader sourceLoader) {
        super(config, sourceLoader);
    }
}
