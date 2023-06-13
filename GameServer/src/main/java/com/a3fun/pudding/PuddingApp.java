package com.a3fun.pudding;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

public class PuddingApp {

    public static void main(String[] args) {
        launch();
    }

    private static void launch() {
        ServerConfigManager.getInstance().loadConfig();

        StaticApplicationContext parent = new StaticApplicationContext();
        parent.getBeanFactory().registerSingleton("serverConfigManager", ServerConfigManager.getInstance());

        parent.refresh();

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext*.xml" }, true, parent);

        Application.setApplicationContext(applicationContext);

        Application.launch();

        Application.initMetaConfig();
    }

}
