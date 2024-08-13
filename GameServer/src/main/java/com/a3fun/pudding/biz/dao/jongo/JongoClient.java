package com.a3fun.pudding.biz.dao.jongo;

import com.a3fun.pudding.ServerConfigManager;
import com.a3fun.pudding.config.MongoClientConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.mongodb.*;
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jongo.Jongo;
import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JongoClient {
    private Jongo jongo;

    private MongoClient mongoClient;

    public JongoClient(String url, String dbName) {
        this(url, dbName, true, "-1");
    }

    public JongoClient(String url, String dbName, boolean isSelfServer, String serverId) {
        MongoClientConfig mongoClientConfig = ServerConfigManager.getInstance().getMongoClientConfig();
        log.info("服务器 id= {} JongoClient {} {} 创建了~ selfJongo = {}, mongoClientConfig is : {} ", serverId, url, dbName, isSelfServer, mongoClientConfig);
        MongoClientURI uri = new MongoClientURI(url);
        List<String> hosts = uri.getHosts();
        if (hosts == null || hosts.size() == 0) {
            throw new RuntimeException("mongodb数据库连接配置的连接地址有问题");
        }
        List<ServerAddress> serverAddresses = hosts.stream().map(ServerAddress::new).toList();
        List<MongoCredential> mongoCredentials = null;
        if (uri.getUsername() != null && uri.getPassword() != null) {
            mongoCredentials = List.of(MongoCredential.createCredential(uri.getUsername(), uri.getDatabase(), uri.getPassword()));
        }
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder(uri.getOptions());
        if(isSelfServer) {
            builder.minConnectionsPerHost(mongoClientConfig.getSelfMinConnectionsPerHost());
            builder.connectionsPerHost(mongoClientConfig.getSelfConnectionsPerHost());
            builder.maxConnectionIdleTime(mongoClientConfig.getSelfMaxConnectionIdleTime());
            builder.maxConnectionLifeTime(mongoClientConfig.getSelfMaxConnectionLifeTime());
            builder.maxWaitTime(mongoClientConfig.getSelfMaxWaitTime());
            builder.connectTimeout(mongoClientConfig.getSelfConnectTimeout());
            builder.threadsAllowedToBlockForConnectionMultiplier(mongoClientConfig.getSelfThreadsAllowedToBlockForConnectionMultiplier());
        } else {
            builder.minConnectionsPerHost(mongoClientConfig.getOtherMinConnectionsPerHost());
            builder.connectionsPerHost(mongoClientConfig.getOtherConnectionsPerHost());
            builder.maxConnectionIdleTime(mongoClientConfig.getOtherMaxConnectionIdleTime());
            builder.maxConnectionLifeTime(mongoClientConfig.getOtherMaxConnectionLifeTime());
            builder.maxWaitTime(mongoClientConfig.getOtherMaxWaitTime());
            builder.connectTimeout(mongoClientConfig.getOtherConnectTimeout());
            builder.threadsAllowedToBlockForConnectionMultiplier(mongoClientConfig.getOtherThreadsAllowedToBlockForConnectionMultiplier());
        }
        builder.addCommandListener(new CommandListener() {
            @Override
            public void commandStarted(CommandStartedEvent event) {
                log.debug("commandStarted: {}", event);
            }

            @Override
            public void commandSucceeded(CommandSucceededEvent event) {
                log.debug("commandSucceeded: {}", event);
            }

            @Override
            public void commandFailed(CommandFailedEvent event) {
                log.debug("commandFailed: {}", event);
            }
        });

        MongoClientOptions options = builder.build();
        if (mongoCredentials != null) {
            this.mongoClient = new MongoClient(serverAddresses, mongoCredentials, options);
        } else {
            this.mongoClient = new MongoClient(serverAddresses, options);
        }
        DB db = this.mongoClient.getDB(dbName);
        JacksonMapper.Builder mapperBuilder = new JacksonMapper.Builder().enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY).disable(MapperFeature.AUTO_DETECT_SETTERS)
                .disable(MapperFeature.AUTO_DETECT_GETTERS).disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
        mapperBuilder.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Mapper mapper = mapperBuilder.build();
        this.jongo = new Jongo(db, mapper);
    }

    public Jongo getJongo() {
        return this.jongo;
    }

    public void close() {
        this.mongoClient.close();
    }


}
