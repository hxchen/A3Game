package com.a3fun.pudding;

import com.a3fun.net.NettyServer;

import java.util.LinkedList;
import java.util.List;

public class PuddingAppCleaner {
    private static PuddingAppCleaner instance = new PuddingAppCleaner();
    private final List<NettyServer> nettyServerList = new LinkedList<>();

    public static PuddingAppCleaner getInstance() {
        return instance;
    }
    public void addNettyServer(NettyServer nettyServer) {
        nettyServerList.add(nettyServer);
    }
}
