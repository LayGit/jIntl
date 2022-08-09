package com.laylib.jintl.provider;

import com.laylib.jintl.config.BaseProviderConfig;
import com.laylib.jintl.entity.SourceIndex;
import com.laylib.jintl.entity.SourceProperties;
import com.laylib.jintl.formatter.DefaultMessageFormatter;
import com.laylib.jintl.formatter.MessageFormatter;
import com.laylib.jintl.formatter.SourceNameFormatter;
import com.laylib.jintl.formatter.SourceNameFormatterFactory;
import com.laylib.jintl.loader.AbstractSourceLoader;
import com.laylib.jintl.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract implementation of the {@link MessageProvider} interface
 *
 * @author Lay
 */
public class DefaultMessageProvider<T extends BaseProviderConfig> implements MessageProvider {

    protected final T config;

    /**
     * source loader
     */
    protected final AbstractSourceLoader<T> sourceLoader;

    /**
     * source name formatter
     */
    protected final SourceNameFormatter sourceNameFormatter;

    /**
     * Cache to hold already loaded properties per filename
     */
    private final ConcurrentMap<String, PropertiesHolder> cachedProperties = new ConcurrentHashMap<>();

    /**
     * Cache to hold already loaded properties per filename
     */
    private final ConcurrentMap<Locale, PropertiesHolder> cachedMergedProperties = new ConcurrentHashMap<>();

    private SourceIndex cachedSourceIndex;

    public DefaultMessageProvider(T config, Class<? extends AbstractSourceLoader<T>> loaderCls) {
        this.config = config;
        this.sourceNameFormatter = SourceNameFormatterFactory.build(config.getSourceNameFormatterClass(), config.getSourceFileExtension());

        try {
            this.sourceLoader = loaderCls.getDeclaredConstructor(config.getClass()).newInstance(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        init();
    }

    protected void init() {
        if (config.getSourceWatchInterval() != null || config.getIndexWatchInterval() != null) {
            this.sourceLoader.withMonitor(this::resolveIndexChanged, this::refreshCache);
        }

        if (config.getAutoLoad()) {
            load();
        }

        if (config.getAutoWatch()) {
            watch();
        }
    }

    /**
     * load index and sources
     */
    public void load() {
        // load index
        SourceIndex index = sourceLoader.loadIndex();
        if (index != null) {
            // load sources
            List<SourceProperties> sourceProperties = sourceLoader.loadSources(index);

            // build cache
            buildCache(sourceProperties);

            // reset cached index
            cachedSourceIndex = index;
        }
    }

    /**
     * watch index or sources changed
     */
    public void watch() {
        this.sourceLoader.startMonitor();
    }

    /**
     * resolve index changed
     * @param changedSourceIndex changed index
     */
    protected void resolveIndexChanged(SourceIndex changedSourceIndex) {
        SourceIndexDiff.DiffResult res = SourceIndexDiff.compare(cachedSourceIndex, changedSourceIndex);
        if (res.hasAdded()) {
            refreshCache(sourceLoader.loadSources(res.getAddedIndex()));
        }

        if (res.hasRemoved()) {
            SourceIndex removedIndex = res.getRemovedIndex();
            for (SourceIndex.IndexItem indexItem : removedIndex.getItems()) {
                for (Locale locale : indexItem.getLocales()) {
                    String sourceName = sourceNameFormatter.format(indexItem.getTag(), locale);
                    this.cachedProperties.remove(sourceName);
                }
            }
            buildMergedCache();
        }
    }

    /**
     * build cache
     * @param sourceProperties
     */
    protected void buildCache(List<SourceProperties> sourceProperties) {
        Map<String, PropertiesHolder> propsMap = new HashMap<>();
        for (SourceProperties properties : sourceProperties) {
            String sourceName = sourceNameFormatter.format(properties.getTag(), properties.getLocale());
            propsMap.put(sourceName, buildPropertiesHolder(properties.getProperties(), properties.getLocale()));
        }

        synchronized (this.cachedProperties) {
            this.cachedProperties.clear();
            this.cachedProperties.putAll(propsMap);
        }

        // refresh merged
        buildMergedCache();
    }

    /**
     * build merged cache
     */
    protected void buildMergedCache() {
        Map<Locale, PropertiesHolder> mergedPropsMap = new HashMap<>();
        for (PropertiesHolder holder : this.cachedProperties.values()) {
            mergedPropsMap.put(holder.locale, mergeHolder(mergedPropsMap.get(holder.locale), holder));
        }

        synchronized (this.cachedMergedProperties) {
            this.cachedMergedProperties.clear();
            this.cachedMergedProperties.putAll(mergedPropsMap);
        }
    }

    private PropertiesHolder mergeHolder(PropertiesHolder src, PropertiesHolder target) {
        PropertiesHolder holder;
        if (src == null) {
            return target;
        }

        holder = buildPropertiesHolder(src.properties, src.locale);
        holder.properties.putAll(target.properties);
        return holder;
    }

    /**
     * refresh sources
     * @param sourceProperties  target source properties
     */
    protected void refreshCache(List<SourceProperties> sourceProperties) {
        if (CollectionUtils.isEmpty(sourceProperties)) {
            return;
        }

        synchronized (this.cachedProperties) {
            for (SourceProperties properties : sourceProperties) {
                String sourceName = sourceNameFormatter.format(properties.getTag(), properties.getLocale());
                this.cachedProperties.put(sourceName, buildPropertiesHolder(properties.getProperties(), properties.getLocale()));
            }
        }

        // refresh merged
        buildMergedCache();
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) {
        String msg = getMessageInternal(code, args, locale);
        if (msg != null) {
            return msg;
        }

        return null;
    }

    @Override
    public String getMessage(String code, Locale locale) {
        return getMessage(code, new Object[0], locale);
    }

    protected String getMessageInternal(String code, Object[] args, Locale locale) {
        PropertiesHolder propsHolder = getMergedProperties(locale);
        if (propsHolder == null) {
            return null;
        }

        if (CollectionUtils.isEmpty(args)) {
            return propsHolder.getProperty(code);
        }

        MessageFormatter messageFormatter = propsHolder.getMessageFormatter(code, locale);
        if (messageFormatter != null) {
            return messageFormatter.format(args);
        }
        return null;
    }

    protected PropertiesHolder getMergedProperties(Locale locale) {
        PropertiesHolder mergedHolder = this.cachedMergedProperties.get(locale);
        if (mergedHolder != null) {
            return mergedHolder;
        }
        return null;
    }

    protected PropertiesHolder buildPropertiesHolder(Properties properties, Locale locale) {
        return new PropertiesHolder(properties, locale, config.getMessageFormatterClass());
    }

    public static class PropertiesHolder {

        private static final Logger logger = LoggerFactory.getLogger(PropertiesHolder.class);

        private final Locale locale;

        private final Properties properties;

        private Class<MessageFormatter> messageFormatterClass;

        private final ConcurrentMap<String, Map<Locale, MessageFormatter>> cachedMessageFormatter = new ConcurrentHashMap<>();

        public PropertiesHolder(Properties properties, Locale locale, Class<MessageFormatter> messageFormatterClass) {
            this.properties = properties;
            this.locale = locale;
            this.messageFormatterClass = messageFormatterClass;
        }

        public String getProperty(String code) {
            if (this.properties == null) {
                return null;
            }
            return this.properties.getProperty(code);
        }

        public MessageFormatter getMessageFormatter(String code, Locale locale) {
            if (this.properties == null) {
                return null;
            }

            Map<Locale, MessageFormatter> localeMap = this.cachedMessageFormatter.get(code);
            if (localeMap != null) {
                MessageFormatter result = localeMap.get(locale);
                if (result != null) {
                    return result;
                }
            }
            String msg = this.properties.getProperty(code);
            if (msg != null) {
                if (localeMap == null) {
                    localeMap = new ConcurrentHashMap<>();
                    Map<Locale, MessageFormatter> existing = this.cachedMessageFormatter.putIfAbsent(code, localeMap);
                    if (existing != null) {
                        localeMap = existing;
                    }
                }
                MessageFormatter result = createMessageFormatter(msg, locale);
                localeMap.put(locale, result);
                return result;
            }
            return null;
        }

        private MessageFormatter createMessageFormatter(String msg, Locale locale) {
            if (this.messageFormatterClass == null) {
                return createDefaultMessageFormatter(msg, locale);
            }

            try {
                return this.messageFormatterClass.getDeclaredConstructor(String.class, Locale.class).newInstance(msg, locale);
            } catch (Exception e) {
                logger.warn("Message Formatter {} constructed failed, use DefaultMessageFormatter instead", this.messageFormatterClass);
                return createDefaultMessageFormatter(msg, locale);
            }
        }

        private MessageFormatter createDefaultMessageFormatter(String msg, Locale locale) {
            return new DefaultMessageFormatter(msg, locale);
        }
    }
}
