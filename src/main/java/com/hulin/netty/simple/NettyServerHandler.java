package com.hulin.netty.simple;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 自定义一个Handler 需要继承netty规定好的某个HandlerAdapter（规范）
 *
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送的数据
     * @param ctx 上下文对象, 含有 管道pipeline , 通道channel, 地址
     * @param msg 就是客户端发送的数据 默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取通道
        Channel channel=ctx.channel();
        //将msg转成一个ByteBuf
        //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
        ByteBuf buf=(ByteBuf) msg;
        System.out.println("客户端发送消息是："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+channel.remoteAddress());
        /**
         * 假设该处获取数据后，需要处理一个很耗时的业务 那么会阻塞在该处，channelReadComplete方法需要该方法执行结束才可以执行
         * 则客户端也会一直等待服务端响应
         * 解决方案一: 用户程序自定义的普通任务
         *           将该处理逻辑提交到workerGroup中的NioEventLoop中的任务队列taskQueue中 实现异步
         *           即该方法不会阻塞，则channelReadComplete会正常执行，客户端会先收到服务端响应
         *           然后等任务队列中的任务执行结束后，再将业务处理结果主动返回给客户端
         */

//        ctx.channel().eventLoop().execute(new Runnable() {
//            public void run() {
//                try {
//                    Thread.sleep(5 * 1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵2", CharsetUtil.UTF_8));
//                    System.out.println("channel code=" + ctx.channel().hashCode());
//                } catch (Exception ex) {
//                    System.out.println("发生异常" + ex.getMessage());
//                }
//            }
//        });
        //解决方案2 : 用户自定义定时任务 -》 该任务是提交到 scheduleTaskQueue中

//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    Thread.sleep(5 * 1000);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵4", CharsetUtil.UTF_8));
//                    System.out.println("channel code=" + ctx.channel().hashCode());
//                } catch (Exception ex) {
//                    System.out.println("发生异常" + ex.getMessage());
//                }
//            }
//        }, 5, TimeUnit.SECONDS);


    }

    /**
     * 写数据给客户端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端",CharsetUtil.UTF_8));
    }

    //处理异常 一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
