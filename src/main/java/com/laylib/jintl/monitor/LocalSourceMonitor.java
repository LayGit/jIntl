package com.laylib.jintl.monitor;

import com.laylib.jintl.config.BaseProviderConfig;
import com.laylib.jintl.entity.SourceIndex;
import com.laylib.jintl.formatter.SourceNameFormatter;
import com.laylib.jintl.formatter.SourceNameFormatterFactory;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Local Source Monitor
 *
 * @author Lay
 */
public class LocalSourceMonitor extends AbstractSourceMonitor<BaseProviderConfig> {

    private static final Logger logger = LoggerFactory.getLogger(LocalSourceMonitor.class);

    /**
     * index file monitor
     */
    private FileAlterationMonitor indexMonitor;

    /**
     * source file monitor
     */
    private FileAlterationMonitor sourceMonitor;

    private final IndexFileChangedListener indexChangedListener;

    private final SourceFileChangedListener sourceChangedListener;

    private volatile boolean indexWatching;

    private volatile boolean sourceWatching;

    private final ConcurrentMap<String, SourceIndex.SingleIndexItem> sourceFileItemMap;

    public LocalSourceMonitor(BaseProviderConfig config, IndexFileChangedListener indexChangedListener, SourceFileChangedListener sourceChangedListener) {
        super(config);
        this.indexChangedListener = indexChangedListener;
        this.sourceChangedListener = sourceChangedListener;
        this.sourceFileItemMap = new ConcurrentHashMap<>();
    }

    @Override
    public void startIndexMonitor() {
        if (indexWatching) {
            return;
        }

        try {
            if (indexMonitor == null) {
                indexMonitor = new FileAlterationMonitor(config.getIndexWatchInterval());
                Path indexPath = Path.of(config.getRoot(), config.getIndex());
                FileAlterationObserver observer = getObserver(indexPath);
                observer.addListener(new FileAlterationListenerAdaptor() {
                    @Override
                    public void onFileChange(File file) {
                        indexChangedListener.onChange(file);
                    }
                });
                indexMonitor.addObserver(observer);
            }

            indexMonitor.start();
            indexWatching = true;
        } catch (Exception e) {
            logger.error("Index Monitor started failed:", e);
        }
    }

    @Override
    public void startSourceMonitor() {
        if (sourceWatching) {
            return;
        }

        try {
            if (sourceMonitor == null) {
                sourceMonitor = new FileAlterationMonitor(config.getSourceWatchInterval());
                IOFileFilter filter = FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter(config.getIndex()));
                FileAlterationObserver observer = new FileAlterationObserver(config.getRoot(), filter);
                FileAlterationListenerAdaptor adaptor = new FileAlterationListenerAdaptor() {
                    @Override
                    public void onFileChange(File file) {
                        SourceIndex.SingleIndexItem singleIndexItem = sourceFileItemMap.get(file.getAbsolutePath());
                        if (singleIndexItem != null) {
                            sourceChangedListener.onChange(singleIndexItem.getTag(), singleIndexItem.getLocale(), file);
                        }
                    }
                };
                observer.addListener(adaptor);
                sourceMonitor.addObserver(observer);
            }

            sourceMonitor.start();
        } catch (Exception e) {
            logger.error("Source Monitor started failed:", e);
        }
    }

    @Override
    public void stopIndexMonitor() {
        if (indexWatching) {
            try {
                indexMonitor.stop();
                indexWatching = false;
            } catch (Exception e) {
                logger.error("Index Monitor stop failed:", e);
            }
        }
    }

    @Override
    public void stopSourceMonitor() {
        if (sourceWatching) {
            try {
                sourceMonitor.stop();
                sourceWatching = false;
            } catch (Exception e) {
                logger.error("Souce Monitor stop failed:", e);
            }
        }
    }

    @Override
    public void watchSourcesWithIndex(SourceIndex sourceIndex) {
        Map<String, SourceIndex.SingleIndexItem> map = new HashMap<>();
        SourceNameFormatter sourceNameFormatter = SourceNameFormatterFactory.build(config.getSourceNameFormatterClass(), config.getSourceFileExtension());

        for (SourceIndex.IndexItem item : sourceIndex.getItems()) {
            if (sourceIndex.getItems().isEmpty()) {
                continue;
            }

            String tag = item.getTag();
            IOFileFilter filter = null;
            for (Locale locale : item.getLocales()) {
                String fileName = sourceNameFormatter.format(tag, locale);
                IOFileFilter nameFilter = FileFilterUtils.nameFileFilter(fileName);
                if (filter == null) {
                    filter = nameFilter;
                } else {
                    filter = filter.and(nameFilter);
                }

                map.put(Path.of(config.getRoot(), tag, fileName).toString(), new SourceIndex.SingleIndexItem(tag, locale));
            }
        }

        synchronized (this.sourceFileItemMap) {
            this.sourceFileItemMap.clear();
            this.sourceFileItemMap.putAll(map);
        }
    }

    public static FileAlterationObserver getObserver(Path path) {
        IOFileFilter filter = FileFilterUtils.nameFileFilter(path.getFileName().toString());
        return new FileAlterationObserver(path.getParent().toString(), filter);
    }

    public interface IndexFileChangedListener {
        void onChange(File file);
    }

    public interface SourceFileChangedListener {
        void onChange(String tag, Locale locale, File file);
    }
}
