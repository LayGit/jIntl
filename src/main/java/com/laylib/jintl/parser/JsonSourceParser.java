package com.laylib.jintl.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * JSON Source Parser
 *
 * @author Lay
 */
public class JsonSourceParser extends AbstractSourceParser {

    public JsonSourceParser(Charset charset) {
        super(charset);
    }

    @Override
    public Properties parse(String raw) {
        try {
            return parse(new ObjectMapper().readTree(raw));
        } catch (Exception ignored) {}
        return null;
    }

    @Override
    public Properties parse(InputStream is) {
        try {
            return parse(new ObjectMapper().readTree(new InputStreamReader(is, getCharset())));
        } catch (Exception ignored) {}
        return null;
    }

    @Override
    public Properties parse(byte[] raw) {
        try {
            return parse(new ObjectMapper().readTree(new InputStreamReader(new ByteArrayInputStream(raw), getCharset())));
        } catch (Exception ignored) {}
        return null;
    }

    private Properties parse(JsonNode jsonNode) {
        final Properties props = new Properties();
        Map<String, String> map = new HashMap<>();
        addKeys("", jsonNode, map);
        props.putAll(map);
        return props;
    }

    private void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }
}
