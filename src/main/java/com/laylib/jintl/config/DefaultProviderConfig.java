package com.laylib.jintl.config;

import com.laylib.jintl.provider.AbstractMessageProvider;
import com.laylib.jintl.provider.DefaultMessageProvider;

/**
 * Local Message Provider Config
 *
 * @author Lay
 */
public class DefaultProviderConfig extends BaseProviderConfig {
    @Override
    public String getType() {
        return "default";
    }

    @Override
    public Class<? extends AbstractMessageProvider<? extends BaseProviderConfig>> getProviderClass() {
        return DefaultMessageProvider.class;
    }
}
