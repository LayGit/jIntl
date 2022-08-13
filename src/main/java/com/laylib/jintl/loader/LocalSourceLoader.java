package com.laylib.jintl.loader;

import com.laylib.jintl.config.LocalProviderConfig;
import com.laylib.jintl.entity.SourceIndex;
import com.laylib.jintl.entity.SourceProperties;
import com.laylib.jintl.monitor.LocalSourceMonitor;
import com.laylib.jintl.monitor.SourceMonitor;
import com.laylib.jintl.parser.SourceParser;
import com.laylib.jintl.parser.SourceParserDetector;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

/**
 * Local Source Loader
 *
 * @author Lay
 */
public class LocalSourceLoader extends AbstractSourceLoader<LocalProviderConfig> {

    private final boolean isClasspath;

    private final String rootPath;

    public LocalSourceLoader(LocalProviderConfig config) {
        super(config);
        if (config.getRoot().startsWith("classpath:")) {
            isClasspath = true;
            rootPath = config.getRoot().substring("classpath:".length());
        } else {
            isClasspath = false;
            rootPath = config.getRoot();
        }
    }

    @Override
    protected SourceMonitor createMonitor() {
        return new LocalSourceMonitor(config, this::onIndexChange, this::onSourceChange);
    }

    protected void onIndexChange(File file) {
        try (InputStream is = new FileInputStream(file)) {
            SourceIndex sourceIndex = loadIndex(is);
            this.indexChangedListener.onChange(sourceIndex);
        } catch (Exception ignored) {}
    }

    protected void onSourceChange(String tag, Locale locale, File file) {
        SourceParser sourceParser = SourceParserDetector.detect(file.getName(), config.getCharset());
        try (InputStream is = new FileInputStream(file)) {
            Properties props = sourceParser.parse(is);
            this.sourceChangedListener.onChange(Collections.singletonList(new SourceProperties(tag, locale, props)));
        } catch (Exception ignored) {}
    }

    @Override
    public SourceIndex loadIndex() {
        Path indexPath = Path.of(rootPath, config.getIndex());
        if (isClasspath) {
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(indexPath.toString())) {
                return loadIndex(is);
            } catch (Exception ignored) {}
        } else {
            try (InputStream is = new FileInputStream(indexPath.toFile())) {
                return loadIndex(is);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private SourceIndex loadIndex(InputStream is) {
        Yaml yaml = new Yaml();
        Map<String, List<String>> props = yaml.load(is);
        return resolveIndex(props);
    }

    @Override
    public List<SourceProperties> loadSources(SourceIndex index) {
        List<SourceProperties> sourceProperties = new ArrayList<>();
        for (SourceIndex.IndexItem indexItem : index.getItems()) {
            for (Locale locale : indexItem.getLocales()) {
                sourceProperties.add(loadSource(indexItem.getTag(), locale));
            }
        }
        return sourceProperties;
    }

    @Override
    public SourceProperties loadSource(String tag, Locale locale) {
        String sourcePath = getSourcePath(tag, locale);
        SourceParser sourceParser = SourceParserDetector.detect(sourcePath, config.getCharset());
        if (isClasspath) {
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(sourcePath)) {
                Properties props = sourceParser.parse(is);
                return new SourceProperties(tag, locale, props);
            } catch (Exception ignored) {}
        } else {
            try (InputStream is = new FileInputStream(sourcePath)) {
                Properties props = sourceParser.parse(is);
                return new SourceProperties(tag, locale, props);
            } catch (Exception ignored) {}
        }
        return null;
    }

    @Override
    public String getSourcePath(String tag, Locale locale) {
        return Path.of(rootPath, tag, sourceNameFormatter.format(tag, locale)).toString();
    }
}
