package com.xiaofei.algorithm.netty.app.chat.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        System.out.println("你好, " + msg);
//        int i = 1 / 0;
        return "你好, " + msg;
    }

    @Override
    public String divideZero(String num) {
        int i = 1 / 0;
        return "exception";
    }
}