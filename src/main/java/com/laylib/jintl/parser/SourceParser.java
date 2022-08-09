package com.laylib.jintl.parser;

import java.io.InputStream;
import java.util.Properties;

/**
 * Source Parser
 *
 * @author Lay
 */
public interface SourceParser {
    /**
     * parse raw string
     * @param raw raw string
     * @return parsed properties
     */
    Properties parse(String raw);

    /**
     * parse input stream
     * @param is input stream
     * @return parsed properties
     */
    Properties parse(InputStream is);

    /**
     * parse raw bytes
     * @param raw raw bytes
     * @return parsed properties
     */
    Properties parse(byte[] raw);
}
