package com.xiaofei.algorithm.netty.util;

import com.google.common.base.Charsets;
import com.xiaofei.algorithm.Constants;
import com.xiaofei.algorithm.netty.webio.Server;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/5/31 16:23
 */


public class NettyUtil {

    public static Logger getLogger(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);

    }

    /**
     * 创建服务器连接
     *
     * @param port
     * @return
     */
    public static ServerSocketChannel getBlockSSC(int port) {
        try {
            return ServerSocketChannel.open().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ServerSocketChannel getBlockSSC() {
        return getBlockSSC(Constants.PORT);
    }

    public static ServerSocketChannel getNoBlockSSC(int port) {
        ServerSocketChannel blockSSC = getBlockSSC(port);
        try {
            blockSSC.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockSSC;
    }

    public static ServerSocketChannel getNoBlockSSC() {
        return getNoBlockSSC(Constants.PORT);
    }

    /**
     * 创建多个客户端连接
     *
     * @param count
     * @param port
     * @return
     */
    public static void createClient(int count, int port) {
        System.out.println("准备创建客户端连接");
        for (int i = 0; i < count; i++) {
            final int j = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000 * (j + 1));
                    SocketChannel channel = SocketChannel.open();
                    channel.connect(new InetSocketAddress("localhost", port));
                    System.out.println(j + "客户端连接成功");
                    ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello" + j);
                    channel.write(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    public static SocketChannel getClient() {
        return getClient(Constants.PORT);
    }
    /**
     * 获取客户端;
     * @param port
     * @return
     */
    public static SocketChannel getClient(int port) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress("localhost", port));
            channel.finishConnect();
            System.out.println("客户端连接成功");
            return channel;
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    public static SocketChannel getClientAndSend(int port, String msg) {
        SocketChannel client = getClient(port);
        try {
            client.write(Charset.defaultCharset().encode(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static void createClient(int count) {
        createClient(count, Constants.PORT);
    }

    public static void createClient() {
        createClient(1);
    }

    /**
     * 解决半包黏包问题
     * @param source
     * @return
     */
    public static Queue<ByteBuffer> split(ByteBuffer source, char c) {
        Queue<ByteBuffer> split = new ArrayDeque<>();

        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == c){
                int len = i - source.position() + 1;
                ByteBuffer splited = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    splited.put(source.get());
                }
                split.add(splited);
            }
        }
        //半包的数据,直接紧凑留住;
        source.compact();
        return split;
    }
    public static void splitAndPrint(ByteBuffer source, char c) {
        Queue<ByteBuffer> split = split(source, c);
        split.forEach(buffer -> {
            buffer.flip();
            System.out.println(StandardCharsets.UTF_8.decode(buffer));
        });
    }
}
