package com.laylib.jintl.provider;

import com.laylib.jintl.config.AbstractProviderConfig;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * base source provider
 *
 * @author Lay
 * @since 1.0.0
 */
public abstract class AbstractMessageProvider implements MessageProvider {

    /**
     * provider config
     */
    protected AbstractProviderConfig config;

    protected Charset charset;

    private final ConcurrentMap<String, Properties> localeCached = new ConcurrentHashMap<>();

    private final Map<String, Map<String, Properties>> sourceCached = new HashMap<>();

    public AbstractMessageProvider(AbstractProviderConfig config, Charset charset) {
        this.config = config;
        this.charset = charset;
        this.preInit();
        this.init();
    }

    private void init() {
        load(loadIndex());

        if (this.config.isWatchIndex()) {
            watchConfig(this::load);
        }
    }

    private void load(Map<String, List<String>> config) {
        if (config != null) {
            for (Map.Entry<String, List<String>> item : config.entrySet()) {
                String tag = item.getKey();
                List<String> locales = item.getValue();

                for (String l : locales) {
                    Locale locale = Locale.forLanguageTag(l);
                    Properties properties = loadProperties(tag, locale);
                    if (properties != null) {
                        refreshSource(tag, locale.toLanguageTag(), properties);
                    }
                }
            }

            buildLocaleCache();

            if (this.config.isWatchSource()) {
                watchSource(config, (tag, locale, properties) -> {
                    refreshSource(tag, locale, properties);
                    buildLocaleCache();
                });
            }
        }
    }

    private void refreshSource(String tag, String locale, final Properties properties) {
        if (!sourceCached.containsKey(tag)) {
            sourceCached.put(tag, new HashMap<>());
        }

        sourceCached.get(tag).put(locale, properties);
    }

    private void buildLocaleCache() {
        Map<String, Properties> cached = new HashMap<>();
        for (Map.Entry<String, Map<String, Properties>> entry : sourceCached.entrySet()) {
            for (Map.Entry<String, Properties> localeProps : entry.getValue().entrySet()) {
                cached.compute(localeProps.getKey(), (s, properties) -> {
                    if (properties == null) {
                        return localeProps.getValue();
                    }

                    Properties props = new Properties();
                    props.putAll(localeProps.getValue());
                    props.putAll(properties);
                    return props;
                });
            }
        }
        localeCached.clear();
        localeCached.putAll(cached);
    }

    @Override
    public String getMessage(String code, Locale locale) {
        Properties props = localeCached.get(locale.toLanguageTag());
        Properties langProps = localeCached.get(locale.getLanguage());
        String message = null;

        // full locale
        if (props != null) {
            message = props.getProperty(code);
        }

        // language only locale
        if (message == null && langProps != null) {
            message = langProps.getProperty(code);
        }

        return message;
    }

    protected void preInit() {}

    protected abstract Map<String, List<String>> loadIndex();

    protected abstract Properties loadProperties(String tag, Locale locale);

    protected abstract void watchConfig(IndexListener listener);

    protected abstract void watchSource(Map<String, List<String>> config, SourceListener sourceListener);
}
