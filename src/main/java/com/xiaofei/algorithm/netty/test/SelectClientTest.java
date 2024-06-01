package com.xiaofei.algorithm.netty.test;

import com.xiaofei.algorithm.Constants;
import com.xiaofei.algorithm.netty.util.NettyUtil;

import java.nio.channels.SocketChannel;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/5/31 18:40
 */


public class SelectClientTest {
    public static void main(String[] args) throws Exception{
        SocketChannel client = NettyUtil.getClient();
        System.out.println("客户端启动");
        client.close();
    }
}
