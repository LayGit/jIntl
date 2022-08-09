package com.laylib.jintl.formatter;

/**
 * Abstract implementation of the {@link SourceNameFormatter} interface
 *
 * @author Lay
 */
public abstract class AbstractSourceNameFormatter implements SourceNameFormatter {
    protected final String fileExtension;

    public AbstractSourceNameFormatter(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
