package com.hulin.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT=6667;
    public GroupChatServer(){
        try {
            selector=Selector.open();
            serverSocketChannel=ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //监听事件
    public void listen(){
            try {
                while (true) {
                    int count = selector.select();
                    if(count>0){
                       Iterator<SelectionKey> iterator= selector.selectedKeys().iterator();
                       while (iterator.hasNext()){
                          SelectionKey key= iterator.next();
                          if(key.isAcceptable()){
                             SocketChannel socketChannel= serverSocketChannel.accept();
                             socketChannel.configureBlocking(false);
                             socketChannel.register(selector,SelectionKey.OP_READ);
                              System.out.println(socketChannel.getLocalAddress()+"上线");
                          }
                          if(key.isReadable()){
                                readData(key);
                          }
                          iterator.remove();
                       }
                    }else {
                        System.out.println("等待....");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //获取客户端数据
    private void readData(SelectionKey key){
        SocketChannel channel=null;
        try {
            //获取该key对应的通道
            channel = (SocketChannel) key.channel();
            //创建buffer 接收客户端通道的数据到buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count=channel.read(buffer);
            if(count>0){
                //把缓冲区数据转成字符串
                String msg=new String(buffer.array());
                System.out.println("from 客户端："+msg);
                //向其他客户端转发消息（去掉该客户端通道）
                sendInfoToOtherClients(msg,channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getLocalAddress()+"离线了");
                //客户端离线 则取消该通道的注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

    //转发消息给其他客户端

    /**
     *
     * @param msg 需要转发的消息
     * @param self 排除本客户端通道
     */
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中");
        //拿到selector中所有注册的key 反向得到所有的通道
        for(SelectionKey key:selector.keys()){
            Channel targetChannel=key.channel();
            //判断是否是客户端通道
            if(targetChannel instanceof SocketChannel&&targetChannel!=self){
                SocketChannel dest=(SocketChannel)targetChannel;
                //将msg存储到服务端的buffer
                ByteBuffer buffer=ByteBuffer.wrap(msg.getBytes());
                //将buffer中数据发送到客户端通道
                dest.write(buffer);

            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer=new GroupChatServer();
        groupChatServer.listen();
    }

}
