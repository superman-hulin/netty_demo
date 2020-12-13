package com.hulin.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//将数据写入文件
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str="hello NIO";
        //创建输出流
        FileOutputStream fileOutputStream=new FileOutputStream("d:\\file01.txt");
        //输出流中包装了nio中的channel组件  则通过输出流获取对应的FileChannel
        FileChannel fileChannel=fileOutputStream.getChannel();
        //创建一个缓冲区
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        //将字符串以字节格式放入buffer
        buffer.put(str.getBytes());
        //将buffer数据写入通道，即写入了文件 因为通道和文件建立了联系
        //缓冲区状态转换
        buffer.flip();
        fileChannel.write(buffer);
        //关闭资源
        fileOutputStream.close();
    }

}
