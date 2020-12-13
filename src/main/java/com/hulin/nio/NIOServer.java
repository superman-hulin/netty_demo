package com.hulin.nio;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

//NIO服务端
public class NIOServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel 相当于ServerSocket
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        //创建Selector对象
        Selector selector=Selector.open();
        //服务端绑定端口6666
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //也需要将ServerSocketChannel注册到Selector中， 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //在selector中注册的所有通道数量
        System.out.println("注册后的selectionkey 数量="+selector.keys().size());
        while (true){
            //该方法是不断轮询监听通道的事件 一旦产生事件 则执行相应逻辑
            if(selector.select(1000)==0){
                continue;
            }
            Set<SelectionKey> selectionKeys=selector.selectedKeys();
            //遍历监听到的所有SelectionKey
            Iterator<SelectionKey> keyIterator=selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey key=keyIterator.next();
                //判断该key是什么事件
                if(key.isAcceptable()){
                    //该事件表示有新的客户端连接
                    SocketChannel socketChannel=serverSocketChannel.accept();
                    //设置SocketChannel为非阻塞
                    socketChannel.configureBlocking(false);
                    //将SocketChannel注册到Selector 关注事件为OP_READ（读取客户端的消息）,同时给socketChannel关联一个Buffer
                    //该buffer是服务端的，用于接收客户端通道中传输的消息
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){
                    //根据key反向获取对应的通道
                    SocketChannel channel=(SocketChannel) key.channel();
                    //获取该通道关联的buffer
                    ByteBuffer buffer=(ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println(new String(buffer.array()));
                }
                //手动从集合中移除当前的selectionKey,防止重复操作
                keyIterator.remove();
            }
        }
    }
}
