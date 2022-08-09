package com.laylib.jintl.entity;

import java.util.Locale;
import java.util.Properties;

/**
 * Source Properties
 *
 * @author Lay
 */
public class SourceProperties {
    /**
     * source tag
     */
    private String tag;

    /**
     * source locale
     */
    private Locale locale;

    /**
     * source properties
     */
    private Properties properties;

    public SourceProperties() {}

    public SourceProperties(String tag, Locale locale, Properties properties) {
        this.tag = tag;
        this.locale = locale;
        this.properties = properties;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
