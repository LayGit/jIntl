package com.laylib.jintl.config;

import com.laylib.jintl.formatter.MessageFormatter;
import com.laylib.jintl.formatter.SourceNameFormatter;
import com.laylib.jintl.formatter.SourceNameFormatterFactory;
import com.laylib.jintl.provider.AbstractMessageProvider;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Abstract Provider Configuation
 *
 * @author Lay
 */
public abstract class BaseProviderConfig {

    private static final String DEFAULT_SOURCE_FILE_EXTENSION = "yaml";

    private static final String DEFAULT_ROOT = "classpath:intl";

    private static final String DEFAULT_INDEX = "index.yaml";

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * source charset
     */
    private Charset charset;

    /**
     * message formatter class
     */
    private Class<MessageFormatter> messageFormatterClass;

    /**
     * source name formatter class
     */
    private Class<SourceNameFormatter> sourceNameFormatterClass;

    /**
     * source file extension
     */
    private String sourceFileExtension;

    /**
     * root of the source path
     */
    private String root;

    /**
     * index filename
     */
    private String index;

    /**
     * index watch interval
     */
    private Long indexWatchInterval;

    /**
     * source watch interval
     */
    private Long sourceWatchInterval;

    /**
     * auto load
     */
    private Boolean autoLoad;

    /**
     * auto watch
     */
    private Boolean autoWatch;

    /**
     * auto warm up
     */
    private Boolean autoWarmUp;

    public abstract String getType();

    public abstract Class<? extends AbstractMessageProvider<? extends BaseProviderConfig>> getProviderClass();

    public SourceNameFormatter getSourceNameFormatter() {
        return SourceNameFormatterFactory.build(getSourceNameFormatterClass(), getSourceFileExtension());
    }

    public Charset getCharset() {
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Class<MessageFormatter> getMessageFormatterClass() {
        return messageFormatterClass;
    }

    public void setMessageFormatterClass(Class<MessageFormatter> messageFormatterClass) {
        this.messageFormatterClass = messageFormatterClass;
    }

    public Class<SourceNameFormatter> getSourceNameFormatterClass() {
        return sourceNameFormatterClass;
    }

    public void setSourceNameFormatterClass(Class<SourceNameFormatter> sourceNameFormatterClass) {
        this.sourceNameFormatterClass = sourceNameFormatterClass;
    }

    public String getSourceFileExtension() {
        if (sourceFileExtension == null) {
            return DEFAULT_SOURCE_FILE_EXTENSION;
        }
        return sourceFileExtension;
    }

    public void setSourceFileExtension(String sourceFileExtension) {
        this.sourceFileExtension = sourceFileExtension;
    }

    public String getRoot() {
        if (root == null) {
            return DEFAULT_ROOT;
        }
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getIndex() {
        if (index == null) {
            return DEFAULT_INDEX;
        }
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Long getIndexWatchInterval() {
        return indexWatchInterval;
    }

    public void setIndexWatchInterval(Long indexWatchInterval) {
        this.indexWatchInterval = indexWatchInterval;
    }

    public Long getSourceWatchInterval() {
        return sourceWatchInterval;
    }

    public void setSourceWatchInterval(Long sourceWatchInterval) {
        this.sourceWatchInterval = sourceWatchInterval;
    }

    public Boolean getAutoLoad() {
        if (autoLoad == null) {
            autoLoad = true;
        }
        return autoLoad;
    }

    public void setAutoLoad(Boolean autoLoad) {
        this.autoLoad = autoLoad;
    }

    public Boolean getAutoWatch() {
        if (autoWatch == null) {
            return true;
        }
        return autoWatch;
    }

    public void setAutoWatch(Boolean autoWatch) {
        this.autoWatch = autoWatch;
    }

    public Boolean getAutoWarmUp() {
        if (autoWarmUp == null) {
            return true;
        }
        return autoWarmUp;
    }

    public void setAutoWarmUp(Boolean autoWarmUp) {
        this.autoWarmUp = autoWarmUp;
    }
}
