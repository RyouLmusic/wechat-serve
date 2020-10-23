package com.hbr.netty;

import com.google.gson.Gson;
import com.hbr.netty.message.MessageContent;
import com.hbr.netty.message.enums.MsgActionEnum;
import com.hbr.pojo.ChatMsg;
import com.hbr.service.ChatMsgService;
import com.hbr.service.impl.ChatMsgServiceImpl;
import com.hbr.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/19 19:37
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private ChannelGroup channelGroup;
    Gson gson = new Gson();

    ChatMsgService chatMsgService;
    // 初始化，static的话，只会初始化一次
    {
        chatMsgService = SpringUtil.getBean(ChatMsgServiceImpl.class);
    }

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.channelGroup = group;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");
        //添加到channelGroup通道组
        if (channelGroup.find(ctx.channel().id()) == null)
            channelGroup.add(ctx.channel());
//        ChannelHandlerPool.addChannel();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        /*System.out.println("收到消息：" + msg.text());
        System.out.println("收到消息：" + msg.content());*/
        // 将获取的Json对象转换成MessageContent
        MessageContent messageContent = gson.fromJson(msg.text(), MessageContent.class);

        System.out.println(messageContent);

        // 得到消息动作
        Integer action = messageContent.getAction();
        // 获得channel
        Channel channel = ctx.channel();
        // 进行根据action的动作类型进行处理
        // 是首次连接
        if (action == MsgActionEnum.CONNECT.type) {
            // 将发送者的用户id和这个channel进行关系连接
            ChannelHandlerPool.addChannel(messageContent.getSenderId(), channel);
            System.out.println("123456789");
        } else if (action == MsgActionEnum.CHAT.type) {
            // 聊天类型，发送过来的是聊天消息
            // 将获取的消息进行包装成可以存储入数据库的pojo
            String senderId = messageContent.getSenderId();
            String receiverId = messageContent.getReceiverId();
            String message = messageContent.getContent();
            Integer signFlag = 0;
            Integer inList = 0; // 没有用到
            // 创建消息对象
            ChatMsg chatMsg = new ChatMsg(senderId, receiverId, message, signFlag, inList);


            Channel channelFromPool = ChannelHandlerPool.getChannel(receiverId);
            if (channelFromPool == null) {
                // 正常情况不会出现这样的
            } else {
                Channel channelFromGroup = channelGroup.find(channelFromPool.id());
                if (channelFromGroup != null) {

                    // 将消息存储到数据库
                    int i = chatMsgService.saveMsg(chatMsg);
                    // 将消息发送给好友
                    String json = gson.toJson(messageContent);
                    channelFromGroup.writeAndFlush(new TextWebSocketFrame(json));
                } else {
                    // 用户处于离线状态
                }
            }

        } else if (action == MsgActionEnum.PULL_FRIEND.type) {
            // A添加好友B之后，B直接把好友在页面上面显示

        }
        /*System.out.println("messageContent" + messageContent);
        System.out.println("message" + messageContent.getContent());*/

        msg.retain();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");
        //从channelGroup通道组删除
        if (channelGroup.find(ctx.channel().id()) == null)
            channelGroup.remove(ctx.channel());
    }
    /**
     * 重写 userEventTriggered()方法以处理自定义事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果该事件表示握手成功，则从该 ChannelPipeline 中移除HttpRequest-Handler，因为将不会接收到任何HTTP消息了
        if (evt instanceof  WebSocketServerProtocolHandler.HandshakeComplete) {
            ctx.pipeline().remove(HttpRequestHandler.class);

            //(1) 通知所有已经连接的 WebSocket 客户端新的客户端已经连接上了
            channelGroup.writeAndFlush(new TextWebSocketFrame(
                    gson.toJson(("Client " + ctx.channel() + " joined"))
            ));
            //(2) 将新的 WebSocket Channel 添加到 ChannelGroup 中，以便它可以接收到所有的消息
            channelGroup.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
