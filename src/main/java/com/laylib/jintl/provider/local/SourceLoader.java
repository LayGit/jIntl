package com.laylib.jintl.provider.local;

import com.laylib.jintl.parser.YamlParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Properties;

/**
 * source loader
 *
 * @author Lay
 * @since 1.0.0
 */
@Slf4j
public class SourceLoader {
    public static Properties fromResources(String filePath, Charset charset) {
        try (InputStream is = SourceLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (isYaml(filePath)) {
                return YamlParser.parse(is, charset);
            } else {
                return isToProps(is, charset);
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    public static Properties fromDisk(Path filePath, Charset charset) {
        try (InputStream is = new FileInputStream(filePath.toFile())) {
            if (isYaml(filePath)) {
                return YamlParser.parse(is, charset);
            } else {
                return isToProps(is, charset);
            }
        } catch (Exception ignore) {
        }
        return null;
    }
    private static Properties isToProps(InputStream is, Charset charset) throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(is, charset));
        return properties;
    }


    private static boolean isYaml(Path path) {
        return FilenameUtils.getExtension(path.toString()).equals("yaml");
    }

    private static boolean isYaml(String path) {
        return FilenameUtils.getExtension(path).equals("yaml");
    }
}
