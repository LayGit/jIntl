package com.laylib.jintl.parser;

import com.laylib.jintl.util.MapUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

/**
 * Yaml Source Parser
 *
 * @author Lay
 */
public class YamlSourceParser extends AbstractSourceParser {

    public YamlSourceParser(Charset charset) {
        super(charset);
    }

    @Override
    public Properties parse(String raw) {
        if (raw != null) {
            Yaml yaml = new Yaml();
            return parse(yaml.loadAll(raw));
        }
        return null;
    }

    @Override
    public Properties parse(InputStream is) {
        if (is != null) {
            Yaml yaml = new Yaml();
            return parse(yaml.loadAll(new InputStreamReader(is, getCharset())));
        }
        return null;
    }

    @Override
    public Properties parse(byte[] raw) {
        return parse(new String(raw, getCharset()));
    }

    private Properties parse(Iterable<Object> iterable) {
        Map<String, Object> map = null;
        for (Object object : iterable) {
            if (object != null) {
                map = MapUtils.asMap(object);
                map = MapUtils.getFlattenedMap(map);
            }
        }
        if (map != null) {
            Properties properties = new Properties();
            properties.putAll(map);
            return properties;
        }
        return null;
    }


}
