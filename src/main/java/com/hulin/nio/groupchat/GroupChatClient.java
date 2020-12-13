package com.hulin.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    private final String HOST="127.0.0.1";
    private final int PORT=6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;
    public GroupChatClient(){
        try {
            selector=Selector.open();
            socketChannel= SocketChannel.open(new InetSocketAddress(HOST,PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            userName=socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName+"is ok ");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //向服务器发送消息
    public void sendInfo(String info){
        info=userName+"说："+info;
        //从客户端buffer数据写入到客户端通道
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务器端回复的消息
    public void readInfo(){
        try {
           int readChannels= selector.select();
           if(readChannels>0){
               Iterator<SelectionKey> iterator=selector.selectedKeys().iterator();
               while (iterator.hasNext()){
                  SelectionKey key= iterator.next();
                  if(key.isReadable()){
                     SocketChannel sc=(SocketChannel) key.channel();
                     //创建一个buffer 用于将客户端通道数据读到客户端buffer
                      ByteBuffer buffer=ByteBuffer.allocate(1024);
                      sc.read(buffer);
                      String msg=new String(buffer.array());
                      System.out.println(msg.trim());
                  }
               }
               iterator.remove();
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final GroupChatClient groupChatClient=new GroupChatClient();
        //启动一个线程 每隔3秒 读取服务器发送的数据
        new Thread(){
            @Override
            public void run() {
                groupChatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        //发送数据给服务端
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s=scanner.nextLine();
            groupChatClient.sendInfo(s);
        }
    }

}
