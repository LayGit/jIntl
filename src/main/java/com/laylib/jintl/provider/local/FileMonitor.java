package com.laylib.jintl.provider.local;

import com.laylib.jintl.provider.IndexListener;
import com.laylib.jintl.provider.SourceListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * file monitor
 *
 * @author Lay
 * @since 1.0.0
 */
@Slf4j
public class FileMonitor {
    /**
     * config monitor
     */
    private static FileAlterationMonitor CONFIG_MONITOR;

    /**
     * source monitor
     */
    private static FileAlterationMonitor SOURCE_MONITOR;

    private static final Set<String> OBSERVER_SOURCES = new HashSet<>();

    public static void initConfigMonitor(Long watchInterval) {
        if (CONFIG_MONITOR == null) {
            CONFIG_MONITOR = new FileAlterationMonitor(watchInterval);
        }
    }

    public static void initSourceMonitor(Long watchInterval) {
        if (SOURCE_MONITOR == null) {
            SOURCE_MONITOR = new FileAlterationMonitor(watchInterval);
        }
    }

    /**
     * add config file to monitor
     * @param configPath
     * @param indexListener
     */
    public static void addConfig(Path configPath, IndexListener indexListener) {
        FileAlterationObserver observer = getObserver(configPath);
        CONFIG_MONITOR.addObserver(observer);
        observer.addListener(new IndexFileListener(indexListener));
    }

    /**
     * add source file to monitor
     * @param sourcePath
     * @param sourceListener
     */
    public static void addSource(String tag, String locale, Charset charset, Path sourcePath, SourceListener sourceListener) {
        synchronized (OBSERVER_SOURCES) {
            if (OBSERVER_SOURCES.contains(sourcePath.toString())) {
                return;
            }

            OBSERVER_SOURCES.add(sourcePath.toString());
        }

        FileAlterationObserver observer = getObserver(sourcePath);
        SOURCE_MONITOR.addObserver(observer);
        observer.addListener(new SourceFileListener(tag, locale, charset, sourceListener));
    }

    public static FileAlterationObserver getObserver(Path path) {
        IOFileFilter filter = FileFilterUtils.nameFileFilter(path.getFileName().toString());
        return new FileAlterationObserver(path.getParent().toString(), filter);
    }

    public static void startConfig() {
        stopConfig();

        try {
            CONFIG_MONITOR.start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("config monitor start failed: ", e);
        }
    }

    public static void stopConfig() {
        try {
            CONFIG_MONITOR.stop();
        } catch (Exception ignore) {
        }
    }

    public static void startSource() {
        stopSource();

        try {
            SOURCE_MONITOR.start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("source monitor start failed: ", e);
        }
    }

    public static void stopSource() {
        try {
            SOURCE_MONITOR.stop();
        } catch (Exception ignore) {
        }
    }
}
