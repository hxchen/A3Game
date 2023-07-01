package com.a3fun.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Netty 游戏服务器
 */
@Slf4j
public class NettyServer {
    private String name;
    private String ip;
    private int port;
    private int workerCount;
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelInboundHandler handler;

    public NettyServer(String name, String ip, int port, int workerCount, ChannelInboundHandler handler) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.workerCount = workerCount;
        this.handler = handler;
    }

    public void start() {
        // TODO Auto-generated method stub
        this.bossGroup = new NioEventLoopGroup(workerCount/4, new DefaultThreadFactory(name + "-netty boss", true));
        this.workerGroup = new NioEventLoopGroup(workerCount, new DefaultThreadFactory(name + "-netty worker", true));
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(handler);
        bootstrap.option(ChannelOption.SO_BACKLOG, 10240) // 设置待接受连接队列的长度，该队列用于放置 3 次握手后建立的连接
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true)   // 关闭Nagle算法，立即发送数据
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            channel = bootstrap.bind(new InetSocketAddress(ip, port)).sync().channel();
            log.info("Netty server started on name:{}, port:{}", name, port);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        // TODO Auto-generated method stub
        if (channel != null) {
            ChannelFuture future = channel.close();
            future.awaitUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

}
