package com.hulin.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//从文件中读取数据
public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        //创建文件的输入流
        File file=new File("d:\\file01.txt");
        FileInputStream inputStream=new FileInputStream(file);
        //获取对应的通道
        FileChannel fileChannel=inputStream.getChannel();
        //创建buffer
        ByteBuffer buffer=ByteBuffer.allocate((int) file.length());
        //将通道数据写入buffer
        fileChannel.read(buffer);
        //将buffer中的字节数据转成string
        System.out.println(new String(buffer.array()));
        inputStream.close();
    }
}
