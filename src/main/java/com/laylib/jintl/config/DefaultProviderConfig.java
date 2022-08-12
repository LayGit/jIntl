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
@ProviderType
public class DefaultProviderConfig extends BaseProviderConfig {

    @Override
    public Class<? extends MessageProvider> getProviderClass() {
        return DefaultMessageProvider.class;
    }

    @Override
    public Class<? extends SourceLoader> getLoaderClass() {
        return LocalSourceLoader.class;
    }
}
