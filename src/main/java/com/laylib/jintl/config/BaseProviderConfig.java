package com.laylib.jintl.config;

import com.laylib.jintl.formatter.MessageFormatter;
import com.laylib.jintl.formatter.SourceNameFormatter;
import com.laylib.jintl.formatter.SourceNameFormatterFactory;
import com.laylib.jintl.loader.SourceLoader;
import com.laylib.jintl.provider.MessageProvider;

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
    protected Charset charset;

    /**
     * message formatter class
     */
    protected Class<? extends  MessageFormatter> messageFormatterClass;

    /**
     * source name formatter class
     */
    protected Class<? extends  SourceNameFormatter> sourceNameFormatterClass;

    /**
     * source file extension
     */
    protected String sourceFileExtension;

    /**
     * root of the source path
     */
    protected String root;

    /**
     * index filename
     */
    protected String index;

    /**
     * auto load
     */
    protected Boolean autoLoad;

    /**
     * auto watch
     */
    protected Boolean autoWatch;

    /**
     * auto warm up
     */
    protected Boolean autoWarmUp;

    public abstract Class<? extends MessageProvider> getProviderClass();

    public abstract Class<? extends SourceLoader> getLoaderClass();

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

    public Class<? extends MessageFormatter> getMessageFormatterClass() {
        return messageFormatterClass;
    }

    public void setMessageFormatterClass(Class<? extends MessageFormatter> messageFormatterClass) {
        this.messageFormatterClass = messageFormatterClass;
    }

    public Class<? extends SourceNameFormatter> getSourceNameFormatterClass() {
        return sourceNameFormatterClass;
    }

    public void setSourceNameFormatterClass(Class<? extends SourceNameFormatter> sourceNameFormatterClass) {
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
