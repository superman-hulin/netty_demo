package com.hulin.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        //创建相关流
        FileInputStream inputStream=new FileInputStream("d:\\a.jpg");
        FileOutputStream outputStream=new FileOutputStream("d:\\a2.jpg");

        //获取各个流对应的通道
        FileChannel sourceCh=inputStream.getChannel();
        FileChannel destCh=outputStream.getChannel();

        //使用transferForm完成拷贝
        destCh.transferFrom(sourceCh,0,sourceCh.size());
        //关闭相关通道和流
        inputStream.close();
        outputStream.close();
    }
}
