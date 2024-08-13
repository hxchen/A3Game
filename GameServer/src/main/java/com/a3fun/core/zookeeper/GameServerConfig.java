package com.a3fun.core.zookeeper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class GameServerConfig extends ZkConfig{
    private int gameServerId;
    private MySQLConfig mySQLConfig;
    private MongoDBConfig mongoDBConfig;
    private List<IZkGameServerConfigListener> listeners;

    private String gameIp;
    private int gamePort;


    public GameServerConfig(SimpleCurator zk, String zkPath, int gameServerId, List<IZkGameServerConfigListener> listeners) {
        super(zk, zkPath);
        this.gameServerId = gameServerId;
        this.listeners = listeners;
        this.mySQLConfig = new MySQLConfig(zk, this.getZkPath()+"/MySql");
        this.mongoDBConfig = new MongoDBConfig(zk, this.getZkPath()+"/MongoDB");
        // 从ZK中读取配置--game_ip
//        this.getDataAndWatchNode(this.getZkPath() + "/game_ip", (path, data) -> {
//            log.info("GameServerConfig dataChanged path:{}, data:{}", path, data);
//            gameIp = data;
//        });

        this.gameIp = zk.getData(this.getZkPath() + "/game_ip");
        // 从ZK中读取配置--game_port
//        this.getDataAndWatchNode(this.getZkPath() + "/game_port", (path, data) -> {
//            log.info("GameServerConfig dataChanged path:{}, data:{}", path, data);
//            gamePort = Integer.parseInt(data);
//        });

        this.gamePort = Integer.parseInt(zk.getData(this.getZkPath() + "/game_port"));

    }

    public void close() {
        log.info("GameServerConfig close, remove all node listeners");
        this.removeAllNodeListeners();
    }
}
