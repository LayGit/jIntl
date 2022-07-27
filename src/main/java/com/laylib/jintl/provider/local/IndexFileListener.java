package com.laylib.jintl.provider.local;

import com.laylib.jintl.provider.IndexListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;

/**
 * local file listener
 *
 * @author Lay
 * @since 1.0.0
 */
@Slf4j
public class IndexFileListener extends FileAlterationListenerAdaptor {

    private final IndexListener indexListener;

    public IndexFileListener(IndexListener indexListener) {
        this.indexListener = indexListener;
    }

    @Override
    public void onFileChange(File file) {
        try {
            log.debug("Index changed");
            Yaml yaml = new Yaml();
            indexListener.onChanged(yaml.load(new FileInputStream(file)));
        } catch (Exception e) {
            log.error("Index load failed: ", e);
        }
    }
}
