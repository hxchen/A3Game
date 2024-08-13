package com.a3fun.pudding;

import com.a3fun.core.common.ConfigLoader;
import com.a3fun.core.zookeeper.ZookeeperConfig;
import com.a3fun.pudding.config.MongoClientConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ServerConfigManager {
    private static final ServerConfigManager instance = new ServerConfigManager();

    public static ServerConfigManager getInstance() {
        return instance;
    }

    @Getter
    private ZookeeperConfig zookeeperConfig;

    @Getter
    private MongoClientConfig mongoClientConfig;

    @PostConstruct
    public void loadConfig(){
        this.zookeeperConfig = ConfigLoader.loadConfig(ZookeeperConfig.class, GameConstants.ZOOKEEPER_CONFIG_NAME);
        this.mongoClientConfig = new MongoClientConfig();
    }
}
