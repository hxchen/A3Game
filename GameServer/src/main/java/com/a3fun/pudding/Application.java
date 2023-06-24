package com.a3fun.pudding;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Application {
    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext applicationContext){
        Application.applicationContext = applicationContext;
    }
    public  ApplicationContext getApplicationContext(){
        return this.applicationContext;
    }
    public static <T> T getBean(Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }

}
