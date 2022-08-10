package com.laylib.jintl.provider;

import com.laylib.jintl.config.BaseProviderConfig;
import com.laylib.jintl.loader.LocalSourceLoader;

/**
 * Default Message Provider
 *
 * @author Lay
 */
public class DefaultMessageProvider extends AbstractMessageProvider<BaseProviderConfig> {
    public DefaultMessageProvider(BaseProviderConfig config) {
        super(config, new LocalSourceLoader(config));
    }
}
