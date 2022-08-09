package com.laylib.jintl.provider;

import com.laylib.jintl.config.DefaultProviderConfig;
import com.laylib.jintl.loader.LocalSourceLoader;

/**
 * Default Message Provider
 *
 * @author Lay
 */
public class DefaultMessageProvider extends AbstractMessageProvider<DefaultProviderConfig> {
    public DefaultMessageProvider(DefaultProviderConfig config) {
        super(config, LocalSourceLoader.class);
    }
}
