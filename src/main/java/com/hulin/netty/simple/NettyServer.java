package com.hulin.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建两个组 BossGroup和WorkerGroup
         * 1. BossGroup只是处理连接请求 WorkerGroup负责通道数据的读写 会调用管道处理具体业务
         * 2. 两个组都是无限循环
         * 3. 组中会含有多个子线程(NioEventLoop) 个数为cpu核数的两倍
         */
        EventLoopGroup bossGroup= new NioEventLoopGroup(1); //也可指定该组中的线程数
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try {
            //创建服务器的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            //服务端参数配置
            bootstrap.group(bossGroup, workerGroup)//设置两个组
                    .channel(NioServerSocketChannel.class) //服务端通道
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程 队列最大连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    //.handler(null) 该handler对应bossGroup
                    //childHandler对应workerGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //通过客户端通道获得对应的管道，并为该管道设置处理器
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("服务器 is ready...");
            //绑定服务器端口， 生成ChannelFuture对象 即异步返回绑定是否成功的结果
            ChannelFuture cf = bootstrap.bind(6666).sync();
            //对 关闭通道事件 进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();//优雅的关闭
            workerGroup.shutdownGracefully();
        }
    }
}
