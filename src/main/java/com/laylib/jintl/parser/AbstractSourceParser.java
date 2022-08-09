package com.laylib.jintl.parser;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Abstract implemetation of the {@link SourceParser} interface
 *
 * @author Lay
 */
public abstract class AbstractSourceParser implements SourceParser {

    /**
     * charset
     */
    private Charset charset;

    public AbstractSourceParser(Charset charset) {
        if (charset == null) {
            this.charset = StandardCharsets.UTF_8;
        } else {
            this.charset = charset;
        }
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
