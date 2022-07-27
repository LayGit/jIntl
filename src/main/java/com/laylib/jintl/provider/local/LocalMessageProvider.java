package com.laylib.jintl.provider.local;

import com.laylib.jintl.config.AbstractProviderConfig;
import com.laylib.jintl.config.LocalMessageProviderConfig;
import com.laylib.jintl.provider.AbstractMessageProvider;
import com.laylib.jintl.provider.IndexListener;
import com.laylib.jintl.provider.SourceListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * local message provider
 *
 * @author Lay
 * @since 1.0.0
 */
@Slf4j
public class LocalMessageProvider extends AbstractMessageProvider {

    public LocalMessageProvider(AbstractProviderConfig config, Charset charset) {
        super(config, charset);
    }

    @Override
    protected void preInit() {
        LocalMessageProviderConfig cfg = getConfig();
        FileMonitor.initConfigMonitor(cfg.getIndexWatchInterval());
        FileMonitor.initSourceMonitor(cfg.getSourceWatchInterval());
    }

    @Override
    protected Map<String, List<String>> loadIndex() {
        if (getConfig().isLocalResources()) {
            return loadIndexFromResources();
        } else {
            return loadIndexFromDisk();
        }
    }

    private Map<String, List<String>> loadIndexFromResources() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(getIndexPath().toString())) {
            return resolveIndex(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, List<String>> loadIndexFromDisk() {
        try (InputStream is = new FileInputStream(getIndexPath().toFile())) {
            return resolveIndex(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Path getIndexPath() {
        LocalMessageProviderConfig config = getConfig();
        return Path.of(config.getRoot(), config.getIndex());
    }

    private Map<String, List<String>> resolveIndex(InputStream is) {
        Yaml yaml = new Yaml();
        return yaml.load(is);
    }

    @Override
    protected Properties loadProperties(String tag, Locale locale) {
        Path yamlPath = getSourceFilePath(tag, locale, "yaml");
        Path propsPath = getSourceFilePath(tag, locale, "properties");

        // load properties
        Properties yaml;
        Properties props;
        if (getConfig().isLocalResources()) {
            yaml = SourceLoader.fromResources(yamlPath.toString(), charset);
            props = SourceLoader.fromResources(propsPath.toString(), charset);
        } else {
            yaml = SourceLoader.fromDisk(yamlPath, charset);
            props = SourceLoader.fromDisk(propsPath, charset);
        }

        Properties properties = new Properties();
        if (props != null) {
            properties.putAll(props);
        }
        if (yaml != null) {
            properties.putAll(yaml);
        }

        if (properties.size() == 0) {
            return null;
        }

        return properties;
    }

    private LocalMessageProviderConfig getConfig() {
        return (LocalMessageProviderConfig) config;
    }

    private Path getSourceFilePath(String tag, Locale locale, String ext) {
        String loc = locale.getLanguage();
        if (StringUtils.isNotEmpty(locale.getCountry())) {
            loc += "_" + locale.getCountry();
        }
        String fileName = String.format("%s_%s.%s", tag, loc, ext);

        return Path.of(getConfig().getRoot(), tag, fileName);
    }

    @Override
    protected void watchConfig(IndexListener listener) {
        FileMonitor.addConfig(getIndexPath(), listener);
        FileMonitor.startConfig();
    }

    @Override
    protected void watchSource(Map<String, List<String>> config, SourceListener sourceListener) {
        for (Map.Entry<String, List<String>> entry : config.entrySet()) {
            String tag = entry.getKey();

            for (String l : entry.getValue()) {
                Locale locale = Locale.forLanguageTag(l);
                Path yamlPath = getSourceFilePath(tag, locale, "yaml");
                Path propsPath = getSourceFilePath(tag, locale, "properties");
                FileMonitor.addSource(tag, l, charset, yamlPath, sourceListener);
                FileMonitor.addSource(tag, l, charset, propsPath, sourceListener);
            }
        }
        FileMonitor.startSource();
    }
}
