package com.laylib.jintl.parser;

import com.laylib.jintl.util.MapUtils;
import com.moandjiezana.toml.Toml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

/**
 * Toml Source Parser
 *
 * @author Lay
 */
public class TomlSourceParser extends AbstractSourceParser {

    public TomlSourceParser(Charset charset) {
        super(charset);
    }

    @Override
    public Properties parse(String raw) {
        if (raw != null) {
            Toml toml = new Toml().read(raw);
            return parse(toml.toMap());
        }
        return null;
    }

    @Override
    public Properties parse(InputStream is) {
        if (is != null) {
            Toml toml = new Toml().read(new InputStreamReader(is, getCharset()));
            return parse(toml.toMap());
        }
        return null;
    }

    @Override
    public Properties parse(byte[] raw) {
        if (raw != null && raw.length > 0) {
            Toml toml = new Toml().read(new InputStreamReader(new ByteArrayInputStream(raw), getCharset()));
            return parse(toml.toMap());
        }
        return null;
    }

    private Properties parse(Map<String, Object> map) {
        Map<String, Object> _map = MapUtils.getFlattenedMap(map);
        Properties properties = new Properties();
        properties.putAll(_map);
        return properties;
    }
}
