package com.hulin.netty.dubborpc.service.impl;

import com.hulin.netty.dubborpc.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String meg) {
        System.out.println("收到客户端消息="+meg);
        //根据meg返回不同的结果
        if(meg!=null){
            return "你好客户端, 我已经收到你的消息 [" + meg + "] " ;
        }
        else return "你好客户端, 我已经收到你的消息 ";
    }
}
