package com.laylib.jintl.config;

/**
 * local source provider config
 *
 * @author Lay
 * @since 1.0.0
 */
public class LocalMessageProviderConfig extends AbstractProviderConfig {

    /**
     * default root path
     */
    private static final String DEFAULT_ROOT = "intl";

    /**
     * default index filename
     */
    private static final String DEFAULT_INDEX = "index.yaml";

    /**
     * default index watch interval
     */
    private static final Long DEFAULT_INDEX_WATCH_INTERVAL = 1000L;

    /**
     * default source watch interval
     */
    private static final Long DEFAULT_SOURCE_WATCH_INTERVAL = 1000L;

    /**
     * is message source files saved in resources
     */
    private boolean isLocalResources = false;

    /**
     * root path for message source files
     */
    private String root;

    /**
     * index filename
     */
    private String index;

    /**
     * index watch interval(millisecond)
     */
    private Long indexWatchInterval;

    /**
     * source watch interval(millisecond)
     */
    private Long sourceWatchInterval;

    @Override
    public String getType() {
        return "local";
    }

    @Override
    public String getProviderClass() {
        return "com.laylib.jintl.provider.local.LocalMessageProvider";
    }

    public boolean isLocalResources() {
        return isLocalResources;
    }

    public void setLocalResources(boolean localResources) {
        isLocalResources = localResources;
    }

    public String getRoot() {
        if (root == null) {
            root = DEFAULT_ROOT;
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
        if (indexWatchInterval == null || indexWatchInterval < 0) {
            return DEFAULT_INDEX_WATCH_INTERVAL;
        }
        return indexWatchInterval;
    }

    public void setIndexWatchInterval(Long indexWatchInterval) {
        this.indexWatchInterval = indexWatchInterval;
    }

    public Long getSourceWatchInterval() {
        if (sourceWatchInterval == null || sourceWatchInterval < 0) {
            return DEFAULT_SOURCE_WATCH_INTERVAL;
        }
        return sourceWatchInterval;
    }

    public void setSourceWatchInterval(Long sourceWatchInterval) {
        this.sourceWatchInterval = sourceWatchInterval;
    }
}
