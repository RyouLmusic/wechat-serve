package com.hbr.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/19 19:43
 */
public class ChannelHandlerPool {

    //可以存储userId与ChannelId的映射表
    public static ConcurrentHashMap<String, ChannelId> channelIdMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    // 根据userId增加channel
    public static void addChannel(String userId, Channel channel) {
        channelMap.put(userId, channel);
    }
    // 获取channel
    public static Channel getChannel(String useId) {
        return channelMap.get(useId);
    }
    // 删除Channel
    public static Channel removeChannel(String useId) {
        return channelMap.remove(useId);
    }
}
