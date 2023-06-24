package com.a3fun.core.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 配置文件加载类
 */
public class ConfigLoader {

    /**
     * 加载JSON 格式的配置
     * @param clazz
     * @param path
     * @return
     * @param <T>
     */

    public static <T> T loadConfig(Class<T> clazz, String path){
        InputStream in = null;
        File configFile = new File(path);
        try{
            if (!configFile.exists()){
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                if (in == null){
                    throw new IllegalArgumentException("Resource not found:" + path);
                }
            }else {
                in = new FileInputStream(configFile);
            }
            T config = parseJson(clazz, in);
            return config;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if (in != null){
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T> T parseJson(Class<T> clazz, InputStream in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        T t = mapper.readValue(in, clazz);
        return t;
    }

    /**
     * 获取配置文件
     * @param filePath
     * @return
     */
    public static File getConfigFile(String filePath) {
        File cfgFile = new File(filePath);
        if (!cfgFile.exists()) {
            URL url = Thread.currentThread().getContextClassLoader().getResource(filePath);
            if (url == null) {
                return null;
            }
            cfgFile = new File(url.getPath());
            if (!cfgFile.exists()) {
                return null;
            }
        }
        return cfgFile;
    }
}
