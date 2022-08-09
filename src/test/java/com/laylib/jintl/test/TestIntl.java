package com.laylib.jintl.test;

import com.laylib.jintl.IntlSource;
import com.laylib.jintl.config.DefaultProviderConfig;
import com.laylib.jintl.config.IntlConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.*;

/**
 * jIntl Tester
 *
 * @author Lay
 */
public class TestIntl {

    private final static DumperOptions OPTIONS = new DumperOptions();

    static{
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        OPTIONS.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        OPTIONS.setIndicatorIndent(2);
        OPTIONS.setIndent(4);
        OPTIONS.setPrettyFlow(true);
    }

    private DefaultProviderConfig getFileConfig() {
        DefaultProviderConfig providerConfig = new DefaultProviderConfig();
        File directory = new File("src/test/resources/intl");
        String root = directory.getAbsolutePath();
        providerConfig.setRoot(root);
        return providerConfig;
    }

    @Test
    public void testGetMessageMethods() {
        // resources
        testGetMessageMethods(new DefaultProviderConfig());

        // file system
        testGetMessageMethods(getFileConfig());
    }

    @Test
    public void testWatchIndex() throws Exception {
        DefaultProviderConfig providerConfig = getFileConfig();
        providerConfig.setIndexWatchInterval(1000L);

        // remove order tag
        Path indexPath = Path.of(providerConfig.getRoot(), providerConfig.getIndex());
        File file = indexPath.toFile();
        Yaml yaml = new Yaml(OPTIONS);
        Map<String, List<String>> index = yaml.load(new FileInputStream(indexPath.toFile()));
        if (index.containsKey("order")) {
            index.remove("order");
            yaml.dump(index, new FileWriter(file.getAbsolutePath()));
        }

        IntlConfig config = new IntlConfig();
        config.setUseCodeAsDefaultMessage(true);
        IntlSource source = new IntlSource(config, providerConfig);
        Locale locale = Locale.ENGLISH;


        // fallback to code
        String code = "order.creationFailed";
        String msg = source.getMessage(code, locale);
        Assertions.assertEquals(code, msg);

        // change config
        index.put("order", Collections.singletonList("en"));
        yaml.dump(index, new FileWriter(file.getAbsolutePath()));

        // sleep
        Thread.sleep(3000L);

        // verify
        String codeValue = "Failed to create order";
        msg = source.getMessage(code, locale);
        Assertions.assertEquals(codeValue, msg);
    }

    @Test
    public void testWatchSource() throws Exception {
        String fileName = "user/user_zh.yaml";

        DefaultProviderConfig providerConfig = getFileConfig();
        providerConfig.setSourceWatchInterval(1000L);
        IntlConfig config = new IntlConfig();

        // init value
        String initValue = "用户名已被占用";
        Path filePath = Path.of(providerConfig.getRoot(), fileName);
        Yaml yaml = new Yaml(OPTIONS);
        Map<String, Map<String, String>> src = new HashMap<>();
        src.put("user", new HashMap<>() {
            {
                put("nameAlreadyExists", initValue);
            }
        });
        yaml.dump(src, new FileWriter(filePath.toString(), providerConfig.getCharset()));

        config.setUseCodeAsDefaultMessage(true);
        IntlSource source = new IntlSource(config, providerConfig);
        Locale locale = Locale.CHINESE;

        String code = "user.nameAlreadyExists";
        String msg = source.getMessage(code, locale);
        Assertions.assertEquals(initValue, msg);

        // change source
        String changedValue = "用户名已存在，换一个试试吧";
        src.put("user", new HashMap<>(){
            {
                put("nameAlreadyExists", changedValue);
            }
        });
        yaml.dump(src, new FileWriter(filePath.toString(), providerConfig.getCharset()));

        // sleep
        Thread.sleep(3000L);

        msg = source.getMessage(code, locale);
        Assertions.assertEquals(changedValue, msg);
    }

    private void testGetMessageMethods(DefaultProviderConfig providerConfig) {
        IntlConfig config = new IntlConfig();
        config.setUseCodeAsDefaultMessage(true);
        config.setFallbackLanguageOnly(true);


        IntlSource source = new IntlSource(config, providerConfig);
        String code = "http.internalServerError";
        Locale locale;
        String msg;
        String defaultMsg;
        Object[] args;

        // getMessage
        locale = Locale.ENGLISH;
        msg = source.getMessage(code, locale);
        Assertions.assertEquals("Internal Server Error", msg);

        // default locale
        Locale.setDefault(Locale.ENGLISH);
        msg = source.getMessage(code, null);
        Assertions.assertEquals("Internal Server Error", msg);

        // getMessage with args
        code = "user.nameAlreadyExists";
        args = new Object[] { "Lay" };
        msg = source.getMessage(code, args, locale);
        Assertions.assertEquals("The name Lay is already exists", msg);

        // getMessage with defaultMessage
        code = "user.loginFailed";
        defaultMsg = "Login Failed";
        msg = source.getMessage(code, defaultMsg, locale);
        Assertions.assertEquals(defaultMsg, msg);

        // getMessage with args and default message
        defaultMsg = "Login Failed: {0}";
        args = new Object[] { "wrong password" };
        msg = source.getMessage(code, args, defaultMsg, locale);
        Assertions.assertEquals(defaultMsg, msg);

        // getMessage fallback to code
        msg = source.getMessage(code, locale);
        Assertions.assertEquals(code, msg);

        // yaml override properties
        locale = Locale.SIMPLIFIED_CHINESE;
        code = "http.badGateway";
        msg = source.getMessage(code, locale);
        Assertions.assertEquals("无法访问服务器", msg);

        // fallback to language without country
        code = "http.gatewayTimeout";
        msg = source.getMessage(code, locale);
        Assertions.assertEquals("网关超时", msg);
    }
}
