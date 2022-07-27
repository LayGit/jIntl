package com.laylib.jintl.provider;

import com.laylib.jintl.config.AbstractProviderConfig;
import com.laylib.jintl.config.IntlConfig;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * source provider factory
 *
 * @author Lay
 * @since 1.0.0
 */
@Slf4j
public class SourceProviderFactory {

    @SuppressWarnings("unchecked")
    public static MessageProvider build(IntlConfig config) {
        String clsName = config.getProviderConfig().getProviderClass();
        try {
            Class<AbstractMessageProvider> providerClass = (Class<AbstractMessageProvider>) Class.forName(clsName);
            // new instance
            return providerClass.getDeclaredConstructor(AbstractProviderConfig.class, Charset.class).newInstance(config.getProviderConfig(), config.getCharset());
        } catch (ClassNotFoundException e) {
            log.error(String.format("No provider class found: %s", clsName), e);
        } catch (Exception ex) {
            log.error("Provider build failed: ", ex);
        }
        return null;
    }
}
