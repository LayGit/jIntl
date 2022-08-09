package com.laylib.jintl.parser;

import com.laylib.jintl.util.StringUtils;
import org.apache.commons.io.FilenameUtils;

import java.nio.charset.Charset;

/**
 * Source Parser Detector
 *
 * @author Lay
 */
public class SourceParserDetector {

    /**
     * detect parser by source name
     * @param sourceName source name
     * @param charset   charset
     * @return source parser
     */
    public static SourceParser detect(String sourceName, Charset charset) {
        String ext = FilenameUtils.getExtension(sourceName);
        if (StringUtils.isNotEmpty(ext)) {
            switch (ext) {
                case "properties":
                    return new PropertiesSourceParser(charset);
                case "toml":
                    return new TomlSourceParser(charset);
                case "json":
                    return new JsonSourceParser(charset);
                default:
            }
        }
        return new YamlSourceParser(charset);
    }
}
