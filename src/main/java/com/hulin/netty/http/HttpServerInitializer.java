package com.hulin.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //该方法用于给管道加入处理器
        ChannelPipeline pipeline=socketChannel.pipeline();
        //HttpServerCodec 是netty 提供的处理http的 编-解码器
        pipeline.addLast("myHttpServerCodec",new HttpServerCodec());
        //增加一个自定义的handler
        pipeline.addLast("myHttpServerHandler",new HttpServerHandler());
    }
}
