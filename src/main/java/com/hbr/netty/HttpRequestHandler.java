package com.hbr.netty;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.RandomAccessFile;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/10/19 20:54
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // (1) 如果请求了 WebSocket 协议升级，则增加引用计数（调用 retain()方法）
        // 并将它传递给下一 个 ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(request.uri())) {
            ctx.fireChannelRead(request.retain());
        } else {
            //(2) 处理 100 Continue 请求以符合 HTTP 1.1 规范
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

            HttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

            boolean keepAlive = HttpUtil.isKeepAlive(request);

            //如果请求了keep-alive，则添加所需要的 HTTP 头信息
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            //(3) 将 HttpResponse 写到客户端
            ctx.writeAndFlush(response);

            //(5) 写 LastHttpContent 并冲刷至客户端
            ChannelFuture future = ctx.writeAndFlush(
                    LastHttpContent.EMPTY_LAST_CONTENT);
            //(6) 如果没有请求keep-alive，则在写操作完成后关闭 Channel
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private static void send100Continue(ChannelHandlerContext context) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        context.writeAndFlush(response);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
