package com.xiaofei.algorithm.netty.webio;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.xiaofei.algorithm.netty.util.ByteBufferUtil.debugRead;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/5/30 10:35
 */


public class Server {
    static final Logger log = org.slf4j.LoggerFactory.getLogger(Server.class);
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> socketChannels = new ArrayList<>();
        while (true){
            //建立客户端连接;
            SocketChannel socketChannel = ssc.accept();
            if (socketChannel != null){
                log.info("客户端连接成功");
                socketChannels.add(socketChannel);
            }
            for (SocketChannel sc : socketChannels){
                if(sc.read(buffer) > 0){
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                    log.debug("after read...{}", sc);
                }
            }

        }
    }
}
