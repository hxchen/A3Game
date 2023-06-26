package com.a3fun.pudding;

import com.a3fun.core.world.WorldScheduler;
import com.a3fun.pudding.service.WorldService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Application implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private static WorldScheduler worldScheduler;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        Application.applicationContext = applicationContext;
    }
    public  ApplicationContext getApplicationContext(){
        return applicationContext;
    }
    public static <T> T getBean(Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }

    public static <T> Map<String, T> getBeanOfType(Class<T> t){
        return applicationContext.getBeansOfType(t);
    }

    public static void initRunTime() {
        WorldService worldService = (WorldService) getBean(WorldService.class);
        worldService.init();
    }

    public static void initWorldScheduler(){
        worldScheduler = new WorldScheduler();
        worldScheduler.start();
    }

}
