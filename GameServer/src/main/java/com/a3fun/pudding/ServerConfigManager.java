package com.a3fun.pudding;

import com.a3fun.pudding.common.ConfigLoader;
import com.a3fun.pudding.zookeeper.ZookeeperConfig;
import lombok.Getter;

public class ServerConfigManager {

    private static final ServerConfigManager instance = new ServerConfigManager();
    public static ServerConfigManager getInstance(){
        return instance;
    }

    @Getter
    private ZookeeperConfig zookeeperConfig;

    public void loadConfig(){
        this.zookeeperConfig = ConfigLoader.loadConfig(ZookeeperConfig.class, GameConstants.ZOOKEEPER_CONFIG_NAME);
    }
}
