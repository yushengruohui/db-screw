package com.yhh.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取properties文件配置工具类。
 *
 * @author yhh 2021-12-21 12:24:27
 **/
public final class PropertyUtil {
    private static final Logger log = LoggerFactory.getLogger(PropertyUtil.class);
    private static final Pattern CONFIG_PLACEHOLDER = Pattern.compile("(\\$\\{)(.+?)(})");

    private PropertyUtil() {
    }

    /**
     * 获取配置文件的指定配置
     *
     * @param configFile   classpath目录下的配置文件[eg: biz/biz.properties]
     * @param key          配置项
     * @param defaultValue 如果配置项不存在则返回指定配置
     * @return
     */
    public static String getString(String configFile, String key, String defaultValue) {
        try (InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile)) {
            Properties properties = new Properties();
            properties.load(resource);
            String property = properties.getProperty(key, defaultValue);
            return parsePlaceholder(property, properties);
        } catch (IOException e) {
            log.error("getString[读取配置项异常，退出程序] || configFile : {} ", configFile, e);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            System.exit(-1);
        }
        return defaultValue;
    }

    /**
     * 解析占位符
     *
     * @param property
     * @param properties
     * @return
     */
    private static String parsePlaceholder(String property, Properties properties) {
        Matcher matcher = CONFIG_PLACEHOLDER.matcher(property);
        StringBuffer s1 = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group(2);
            matcher.appendReplacement(s1, properties.getProperty(group));
        }
        matcher.appendTail(s1);
        return s1.toString();
    }

    /**
     * 获取配置文件的所有配置
     *
     * @param configFile classpath目录下的配置文件[eg: biz/biz.properties]
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getConfig(String configFile) {
        Map<String, Object> map = new LinkedHashMap<>();
        try (InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile)) {
            Properties properties = new Properties();
            properties.load(resource);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String k = entry.getKey().toString();
                String v = entry.getValue().toString();
                v = parsePlaceholder(v, properties);
                map.put(k, v);
            }
        } catch (IOException e) {
            log.error("getConfig[读取配置文件异常，退出程序] || configFile : {} ", configFile, e);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            System.exit(-1);
        }
        return map;
    }

}