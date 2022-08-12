package com.laylib.jintl.provider;

import com.laylib.jintl.config.BaseProviderConfig;
import com.laylib.jintl.loader.SourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message Provider Factory
 *
 * @author Lay
 */
public class MessageProviderFactory {

    private static final Logger logger = LoggerFactory.getLogger(MessageProviderFactory.class);

    @SuppressWarnings("unchecked")
    public static MessageProvider build(BaseProviderConfig config) {
        try {
            SourceLoader sourceLoader = (SourceLoader) config.getLoaderClass().getConstructors()[0].newInstance(config);
            return (MessageProvider) config.getProviderClass().getConstructors()[0].newInstance(config, sourceLoader);
        } catch (Exception e) {
            logger.error("Message Provider build failed:", e);
        }
        return null;
    }
}
