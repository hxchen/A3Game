package com.a3fun.pudding;

import com.a3fun.core.common.ConfigLoader;
import com.a3fun.core.zookeeper.ZookeeperConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ServerConfigManager {


    @Getter
    private ZookeeperConfig zookeeperConfig;
    @PostConstruct
    public void loadConfig(){
        this.zookeeperConfig = ConfigLoader.loadConfig(ZookeeperConfig.class, GameConstants.ZOOKEEPER_CONFIG_NAME);
    }
}
