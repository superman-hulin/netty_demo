package com.hulin.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//读取文件a 写入文件b
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        //读取文件a 创建输入流
        FileInputStream inputStream=new FileInputStream("1.txt");
        //创建对应的通道
         FileChannel fileChannel = inputStream.getChannel();
         //写入文件b 创建输出流
        FileOutputStream outputStream=new FileOutputStream("2.txt");
        FileChannel fileChannel2=outputStream.getChannel();
        //创建buffer
        ByteBuffer byteBuffer=ByteBuffer.allocate(512);
        while (true){
            //清空buffer
            byteBuffer.clear();
            //将通道数据读入buffer
           int read= fileChannel.read(byteBuffer);
           if(read==-1){ //表示读完
               break;
           }
           //将buffer中的数据读入fileChannel2
            //转换buffer状态
            byteBuffer.flip();
           fileChannel2.write(byteBuffer);
        }
        //关闭相关的流
        inputStream.close();
        outputStream.close();
    }
}
