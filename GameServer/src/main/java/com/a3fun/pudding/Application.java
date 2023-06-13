package com.a3fun.pudding;

import com.a3fun.pudding.config.service.ConfigServiceImpl;
import com.a3fun.pudding.zookeeper.ConfigCenter;
import org.springframework.context.ApplicationContext;

public class Application {
    private static ApplicationContext applicationContext;

    private static ConfigCenter configCenter;
    public static void setApplicationContext(ApplicationContext applicationContext){
        Application.applicationContext = applicationContext;
        configCenter = getBean(ConfigCenter.class);
    }
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static void launch(){
        configCenter.launch();
    }
    public static <T> T getBean(Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }

    public static void initMetaConfig() {
        ConfigServiceImpl configService = getBean(ConfigServiceImpl.class);
        configService.start();
    }
}
