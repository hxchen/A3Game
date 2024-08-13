package com.a3fun.pudding.config;

import lombok.Data;

@Data
public class MongoClientConfig {
    private int selfMinConnectionsPerHost = 1;
    private int selfConnectionsPerHost = 100;
    private int selfMaxConnectionIdleTime = 0 ;
    private int selfMaxConnectionLifeTime = 0;
    private int selfMaxWaitTime = 1000 * 60 * 2;
    private int selfConnectTimeout = 1000 * 60 * 1;
    private int selfThreadsAllowedToBlockForConnectionMultiplier = 5;

    private int otherMinConnectionsPerHost = 1;
    private int otherConnectionsPerHost = 10;
    private int otherMaxConnectionIdleTime = 0;
    private int otherMaxConnectionLifeTime = 0;
    private int otherMaxWaitTime = 1000 * 60 * 2;
    private int otherConnectTimeout = 1000 * 60 * 1;
    private int otherThreadsAllowedToBlockForConnectionMultiplier = 50;

    public MongoClientConfig() {}

    @Override
    public String toString() {
        return "MongoClientConfig{" +
                "selfMinConnectionsPerHost=" + selfMinConnectionsPerHost +
                ", selfConnectionsPerHost=" + selfConnectionsPerHost +
                ", selfMaxConnectionIdleTime=" + selfMaxConnectionIdleTime +
                ", selfMaxConnectionLifeTime=" + selfMaxConnectionLifeTime +
                ", selfMaxWaitTime=" + selfMaxWaitTime +
                ", selfConnectTimeout=" + selfConnectTimeout +
                ", selfThreadsAllowedToBlockForConnectionMultiplier=" + selfThreadsAllowedToBlockForConnectionMultiplier +
                ", otherMinConnectionsPerHost=" + otherMinConnectionsPerHost +
                ", otherConnectionsPerHost=" + otherConnectionsPerHost +
                ", otherMaxConnectionIdleTime=" + otherMaxConnectionIdleTime +
                ", otherMaxConnectionLifeTime=" + otherMaxConnectionLifeTime +
                ", otherMaxWaitTime=" + otherMaxWaitTime +
                ", otherConnectTimeout=" + otherConnectTimeout +
                ", otherThreadsAllowedToBlockForConnectionMultiplier=" + otherThreadsAllowedToBlockForConnectionMultiplier +
                '}';
    }
}
