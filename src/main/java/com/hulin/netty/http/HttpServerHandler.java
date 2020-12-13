package com.hulin.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest httpRequest=(HttpRequest)msg;
            //回复消息给浏览器
            ByteBuf buf= Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);
            //构造http响应
            FullHttpResponse response= new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,buf);
            //设置请求头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
            //将构建好 response返回
            channelHandlerContext.writeAndFlush(response);


        }
    }
}
