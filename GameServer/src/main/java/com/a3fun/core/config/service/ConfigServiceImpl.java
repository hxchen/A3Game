package com.a3fun.core.config.service;

import com.a3fun.core.common.IConfigChangeListener;
import com.a3fun.core.config.model.ConfigMetaException;
import com.a3fun.core.config.annotation.Config;
import com.a3fun.core.config.annotation.ConfigResolver;
import com.a3fun.core.config.model.ConfigFileInfo;
import com.a3fun.core.config.model.ConfigFormat;
import com.a3fun.core.config.model.ConfigHolder;
import com.a3fun.pudding.util.MessageDigestUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ConfigServiceImpl {

    @Value("${config.packages}")
    private String[] configPackages;

    private Map<Class<?>, ConfigHolder> configMap = new HashMap<>();

    private Map<ConfigFormat, ConfigResolver> resolverMap;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DefaultConfigLoader defaultConfigLoader;

    private List<IConfigChangeListener> listeners = new ArrayList<>();
    @PostConstruct
    public void init() {
        log.info("配置文件初始化开始");
        try {
            Map<ConfigFormat, ConfigResolver> resolverMap = new EnumMap<>(ConfigFormat.class);
            Map<String, ConfigResolver> beansOfResolver = applicationContext.getBeansOfType(ConfigResolver.class, false, true);
            for (ConfigResolver b : beansOfResolver.values()) {
                ConfigResolver exists = resolverMap.put(b.type(), b);
                if (exists != null) {
                    throw new IllegalStateException("ConfigResolver duplicate: " + exists.type());
                }
            }
            this.resolverMap = Collections.unmodifiableMap(resolverMap);
            Map<String, IConfigChangeListener> beansOfListener = applicationContext.getBeansOfType(IConfigChangeListener.class, false, true);
            for (IConfigChangeListener listener : beansOfListener.values()) {
                if (listeners.contains(listener)) {
                    throw new IllegalStateException("ConfigListener duplicate: " + listener.getClass().getName());
                }
                listeners.add(listener);
            }

            for (String configPackage : configPackages){
                Reflections reflections = new Reflections(configPackage);
                Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(Config.class);
                for (Class<?> clazz : configClasses) {
                    ConfigHolder configHolder = configDataLoader(clazz);
                    configMap.put(clazz, configHolder);
                }
            }

        } catch (Exception e) {
            log.error("Error in init Config Service" + e.getMessage());
        }

    }
    private ConfigHolder configDataLoader(Class<?> clazz) {
        Config configAnno = clazz.getAnnotation(Config.class);
        String configName = configAnno.name();

        ConfigFormat configFormat = configAnno.format();
        ConfigResolver configResolver = resolverMap.get(configFormat);
        if (configResolver == null) {
            throw new ConfigMetaException("Can't found config loader. configName=" + configName + ", configFormat=" + configFormat);
        }

        try {
            long start = System.currentTimeMillis();

            ConfigFileInfo cfi = defaultConfigLoader.load(configName);

            Object config = configResolver.resolve(clazz, configAnno, cfi.getContent());

            String md = MessageDigestUtils.sha1(cfi.getContent());
            ConfigHolder configHolder = new ConfigHolder(cfi.getBaseInfo(), md, config);

            long end = System.currentTimeMillis();

            log.info("Load config: {}, took {} ms to load", configName, (end - start));

            return configHolder;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ConfigMetaException("config resolver failed. configName: " + configName, e);
        }
    }

    public <T> T getConfig(Class<T> clazz) {
        ConfigHolder configHolder = configMap.get(clazz);
        if (configHolder == null) {
            return null;
        }
        T config = (T) configHolder.getConfig();
        return config;
    }
}
