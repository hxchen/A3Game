package com.a3fun.pudding.config.service;

import com.a3fun.pudding.config.annotation.Config;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import java.util.Set;
@Slf4j
public class ConfigServiceImpl {

    @Setter
    private String[] configPackages;
    public void start() {
        for (String configPackage : configPackages){
            Reflections reflections = new Reflections(configPackage);
            Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(Config.class);
            for (Class<?> clazz : configClasses) {
                log.info("获取配置类：{}", clazz);
            }
        }
    }
}
