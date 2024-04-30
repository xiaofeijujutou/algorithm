package com.xiaofei.algorithm.nio.selector;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/4/30 14:27
 */

import com.xiaofei.algorithm.Constants;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * 目标：客户端案例实现-基于NIO非阻塞通信
 */
public class Client {
    public static void main(String[] args) throws Exception {
        //1.与服务器建立链接;
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", Constants.PORT));
        //2.切换为非阻塞模式
        sChannel.configureBlocking(false);
        //3.分配指定缓存区大小
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //4.发送数据给服务端
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("请输入：");
            String msg = sc.nextLine();
            buf.put(("波仔：" + msg).getBytes());
            buf.flip();
            //5.通道直接写入数据就相当于发送数据
            sChannel.write(buf);
            buf.clear();
        }
    }
}

