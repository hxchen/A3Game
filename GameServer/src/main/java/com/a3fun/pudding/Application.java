package com.a3fun.pudding;

import com.a3fun.core.threads.MainWorker;
import com.a3fun.core.world.WorldScheduler;
import com.a3fun.core.zookeeper.ConfigCenter;
import com.a3fun.core.zookeeper.GameServerConfig;
import com.a3fun.net.NettyServer;
import com.a3fun.pudding.net.GameServerInitializer;
import com.a3fun.pudding.service.WorldService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Application implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    private static WorldScheduler worldScheduler;
    private static ConfigCenter configCenter;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        Application.applicationContext = applicationContext;
        configCenter = getBean(ConfigCenter.class);
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
        MainWorker mainWorker = getBean(MainWorker.class);
        mainWorker.start();

        WorldService worldService = getBean(WorldService.class);
        worldService.init();
    }

    public static void initWorldScheduler(){
        worldScheduler = new WorldScheduler();
        worldScheduler.start();
    }

    /**
     * 启动Netty
     */
    public static void launchNettyServer() {
        GameServerConfig gameServerConfig = configCenter.getGameServerConfig();
        NettyServer nettyServer = new NettyServer("game server", gameServerConfig.getGameIp(), gameServerConfig.getGamePort(), 8, new GameServerInitializer());
        nettyServer.start();
        PuddingAppCleaner.getInstance().addNettyServer(nettyServer);
    }

}
