package com.hbr.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/19 19:19
 */
public class NettyWebSocketServer {

    //channelGroup通道组
    public ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private int port;

    public NettyWebSocketServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new NettyWebSocketServerInitializer(channelGroup));

            ChannelFuture future = bootstrap.bind(new InetSocketAddress(port)).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }
}
