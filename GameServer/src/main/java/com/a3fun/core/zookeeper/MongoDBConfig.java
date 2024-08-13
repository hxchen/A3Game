package com.a3fun.core.zookeeper;

import lombok.Getter;

public class MongoDBConfig extends ZkConfig {
    @Getter
    private String name;
    @Getter
    private String url;

    public MongoDBConfig(SimpleCurator zk, String zkPath) {
        super(zk, zkPath);
        this.name = zk.getData(this.getZkPath() + "/name");
        this.url = zk.getData(this.getZkPath() + "/url");
    }

    @Override
    public String toString() {
        return "MongoDBConfig{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
