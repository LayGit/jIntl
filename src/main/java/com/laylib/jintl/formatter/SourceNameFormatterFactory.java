package com.laylib.jintl.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Source Name Formatter Factory
 *
 * @author Lay
 */
public class SourceNameFormatterFactory {

    private static final Logger logger = LoggerFactory.getLogger(SourceNameFormatterFactory.class);

    private static final String DEFAULT_EXTENSION = "yaml";

    public static SourceNameFormatter build(Class<SourceNameFormatter> cls, String fileExtension) {
        if (fileExtension == null) {
            fileExtension = DEFAULT_EXTENSION;
        }

        if (cls == null) {
            return new DefaultSourceNameFormatter(fileExtension);
        }

        try {
            return cls.getDeclaredConstructor(String.class).newInstance(fileExtension);
        } catch (Exception e) {
            logger.warn("SourceNameFormatter {} could not found, use DefaultSourceNameFormatter instead", cls.getName());
            return new DefaultSourceNameFormatter(fileExtension);
        }
    }
}
