package com.hulin.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 客户端发送消息到服务端
 *      客户端需要一个自定义的handler（MyClientHandler）产生和接收数据，还需要一个编码（出站的）handler
 *      服务端需要一个解码handler（入站），再需要一个自定义的业务处理handler（MyServerHandler）
 * 服务端再发送消息到客户端
 *      服务端需要一个自定义的业务处理handler（MyServerHandler）产生数据，还需要一个编码（出站）handler
 *      客户端需要一个解码handler（入站） 再需要一个自定义的业务处理handler（MyClientHandler）
 */
public class MyServer {
    public static void main(String[] args) throws Exception{

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(new MyServerInitializer()); //自定义一个初始化类


            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
