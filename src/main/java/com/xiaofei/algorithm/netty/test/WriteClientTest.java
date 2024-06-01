package com.xiaofei.algorithm.netty.test;

import com.xiaofei.algorithm.Constants;
import com.xiaofei.algorithm.netty.util.ByteBufferUtil;
import com.xiaofei.algorithm.netty.util.NettyUtil;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/5/31 18:40
 */


public class WriteClientTest {
    public static void main(String[] args) throws Exception{
        SocketChannel client = NettyUtil.getClient();
        client.configureBlocking(true);
        System.out.println("客户端启动");
        int count = 0;
        ByteBuffer buffer = ByteBuffer.allocate(100000000);
        while (true){
            int read = client.read(buffer);
            count += read;
            System.out.println(count);
            buffer.clear();
        }

        //client.close();
    }
}
