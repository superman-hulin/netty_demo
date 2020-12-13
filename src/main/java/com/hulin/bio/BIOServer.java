package com.hulin.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSocket 指定服务端 端口
        ServerSocket serverSocket=new ServerSocket(6666);
        System.out.println("服务端启动了");
        //创建线程池，当有客户端发连接请求时，分配一个线程
        ExecutorService pool= Executors.newCachedThreadPool();
        //服务端循环监听连接
        while (true){
            System.out.println("等待连接");
            //该方法是阻塞等待连接
          final Socket socket= serverSocket.accept();
            System.out.println("连接到一个客户端");
            //分配线程处理两者的通讯 即将通讯任务提交给线程
            pool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    //通讯方法
    public static void handler(Socket socket){
        //存放数据的字节数组
        byte[] bytes=new byte[1024];
        //获取输入流 用于服务端读取该数据
        try {
            InputStream inputStream=socket.getInputStream();
            //循环读取客户端发送的数据
            while (true){
                System.out.println("read....");
               int read= inputStream.read(bytes);
               //当读完时 read为-1
               if(read!=-1){
                   //输出客户端发送的数据  字节转字符串
                   System.out.println(new String(bytes,0,read));
               }
               else break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
