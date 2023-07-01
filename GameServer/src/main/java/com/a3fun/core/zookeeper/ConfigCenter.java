package com.a3fun.core.zookeeper;

import com.a3fun.pudding.ServerConfigManager;
import com.a3fun.pudding.util.OsUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 读取并缓存ZK中的文件信息
 */
@Slf4j
@Service
public class ConfigCenter {
    /**
     * ZK 客户端
     */
    private SimpleCurator zk;
    /**
     * ZK 上的所有配置
     */
    private RootConfig config;
    /**
     * 本机所有网卡IP地址
     */
    private List<String> localhostIpList;
    /**
     * 本进程对应的GameServer配置
     */
    private GameServerConfig currentGameServerConfig;

    @Autowired
    ServerConfigManager serverConfigManager;


    public ConfigCenter(){
    }

    @PostConstruct
    public void init(){
        log.info("配置中心初始化开始");
        ExecutorService executor = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder().setNameFormat("ConfigCenter-zk-").build());
        zk = new SimpleCurator(serverConfigManager.getZookeeperConfig().getConnectString(), executor);
        config = new RootConfig(zk, serverConfigManager.getZookeeperConfig().getConfigPath());
        this.currentGameServerConfig = findCurrentGameServerConfig("gameServerId");
    }
    public void launch(){
        // TODO 一些初始化操作
    }
    /**
     *
     * @param envKeyServerId
     * @return
     */
    private GameServerConfig findCurrentGameServerConfig(String envKeyServerId) {
        GameServerConfig rtn = findCurrentGameServerConfigByEnv(envKeyServerId);
        rtn = rtn == null ? findCurrentGameServerConfigByIP() : rtn;
        return rtn;
    }

    private GameServerConfig findCurrentGameServerConfigByEnv(String envKeyServerId) {
        if (!System.getenv().containsKey(envKeyServerId)) {
            return null;
        }
        String envServerId = System.getenv(envKeyServerId);
        Integer serverId;
        try {
            serverId = Integer.valueOf(envServerId.trim());
        } catch (NumberFormatException e) {
            log.warn("ZkConfig 从环境变量" + envKeyServerId + "=" + envServerId + "获取ServerID，格式化时报错：" + envServerId, e);
            return null;
        }
        GameServerConfig rtn = config.getGameServers().get(serverId);
        log.info("ZkConfig 从环境变量" + envKeyServerId + "=" + envServerId + "获取CurrentGameServerConfig：" + rtn);
        return rtn;
    }

    /**
     * 通过本机IP地址获取当前进程对应的GameServer配置
     * @return
     */
    private GameServerConfig findCurrentGameServerConfigByIP() {
        localhostIpList = OsUtil.getLocalIpList(true);
        log.debug("ZkConfig本机IpList：" + localhostIpList);

        for (GameServerConfig gameServer : config.getGameServers().values()) {
            if (localhostIpList.contains(gameServer.getGameIp())) {
                log.debug("ZkConfig 从IP地址" + gameServer.getGameIp() + "获取CurrentGameServerConfig：" + gameServer);
                return gameServer;
            }
        }
        return null;
    }

    /**
     * 关闭
     */
    public void close() {
        zk.close();
    }

    /**
     * 获取当前进程对应的GameServer配置
     * @return
     */
    public GameServerConfig getGameServerConfig() {
        return currentGameServerConfig;
    }
}
