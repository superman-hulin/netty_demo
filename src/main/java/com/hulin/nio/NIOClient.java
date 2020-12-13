package com.hulin.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        //获取客户端通道
        SocketChannel socketChannel=SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress=new InetSocketAddress("127.0.0.1",6666);
        //如果没连接 则先做其他事
        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("客户端不会阻塞 可以做其他工作");
            }
        }
        String str="hello";
        ByteBuffer buffer=ByteBuffer.wrap(str.getBytes());
        //发送数据 将buffer数据写入通道
        socketChannel.write(buffer);

    }
}
