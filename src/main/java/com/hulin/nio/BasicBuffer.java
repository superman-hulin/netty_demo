package com.hulin.nio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Buffer的使用
 */
public class BasicBuffer {
    public static void main(String[] args) {
        //创建指定大小容量的buffer
       IntBuffer buffer= IntBuffer.allocate(5);
       //向buffer存放数据
        for(int i=0;i<buffer.capacity();i++){
            buffer.put(i*2);
        }
        //从buffer读取数据
        //读取前必须先将buffer的写状态切换为读状态
        buffer.flip();
        buffer.position(1);
        System.out.println(buffer.get());
        buffer.limit(3);
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
        //类型化方式放入数据 buffer的类型需要子类类型
        ByteBuffer byteBuffer=ByteBuffer.allocate(64);
        byteBuffer.putInt(10);
        //类型化取数据
        byteBuffer.flip();
        byteBuffer.getInt();

        //创建一个只读Buffer 则对该buffer put操作会报异常
        ByteBuffer readOnlyBuffer=byteBuffer.asReadOnlyBuffer();

    }
}
