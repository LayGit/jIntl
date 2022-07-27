package com.laylib.jintl.provider.local;

import com.laylib.jintl.provider.SourceListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * source file listener
 *
 * @author Lay
 * @since 1.0.0
 */
@Slf4j
public class SourceFileListener extends FileAlterationListenerAdaptor {

    private final String tag;

    private final String locale;

    private final Charset charset;

    private final SourceListener sourceListener;

    public SourceFileListener(String tag, String locale, Charset charset, SourceListener sourceListener) {
        this.tag = tag;
        this.locale = locale;
        this.charset = charset;
        this.sourceListener = sourceListener;
    }

    @Override
    public void onFileChange(File file) {
        log.debug("source changed: {}", file.getAbsolutePath());
        Properties properties = SourceLoader.fromDisk(file.toPath(), charset);
        sourceListener.onChanged(tag, locale, properties);
    }
}
