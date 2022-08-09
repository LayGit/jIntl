package com.laylib.jintl.parser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Properties Source Parser
 *
 * @author Lay
 */
public class PropertiesSourceParser extends AbstractSourceParser {

    public PropertiesSourceParser(Charset charset) {
        super(charset);
    }

    @Override
    public Properties parse(String raw) {
        final Properties props = new Properties();
        try {
            props.load(new ByteArrayInputStream(raw.getBytes(getCharset())));
        } catch (IOException ignored) {
            return null;
        }
        return props;
    }

    @Override
    public Properties parse(InputStream is) {
        final Properties props = new Properties();
        try (InputStreamReader sr = new InputStreamReader(is, getCharset())) {
            props.load(sr);
        } catch (IOException ignored) {
            return null;
        }
        return props;
    }

    @Override
    public Properties parse(byte[] raw) {
        return parse(new String(raw, getCharset()));
    }
}
