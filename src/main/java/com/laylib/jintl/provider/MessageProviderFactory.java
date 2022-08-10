package com.laylib.jintl.provider;

import com.laylib.jintl.config.BaseProviderConfig;
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
    public static <T extends AbstractMessageProvider<C>, C extends BaseProviderConfig> T build(C config) {
        try {
            return (T) config.getProviderClass().getConstructors()[0].newInstance(config);
        } catch (Exception e) {
            logger.error("Message Provider build failed:", e);
        }
        return null;
    }
}
