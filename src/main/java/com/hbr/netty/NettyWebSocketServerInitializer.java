package com.hbr.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/19 19:38
 */
public class NettyWebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    public ChannelGroup channelGroup;
    public NettyWebSocketServerInitializer (ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(64 * 1024))
                .addLast(new HttpRequestHandler("/ws"))
                .addLast(new WebSocketServerProtocolHandler("/ws", "websocket", true))
                .addLast(new TextWebSocketFrameHandler(channelGroup));

    }
}
