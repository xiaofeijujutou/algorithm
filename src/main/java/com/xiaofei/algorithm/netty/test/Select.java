package com.xiaofei.algorithm.netty.test;

import com.xiaofei.algorithm.netty.util.ByteBufferUtil;
import com.xiaofei.algorithm.netty.util.NettyUtil;
import com.xiaofei.algorithm.netty.webio.Server;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/5/31 16:10
 */


public class Select {
    public static final Logger log = NettyUtil.getLogger(Select.class);
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = NettyUtil.getNoBlockSSC();
        //创建selector
        Selector selector = Selector.open();
        SelectionKey managerKey = ssc.register(selector, 0, null);
        //设置感兴趣的事件
        managerKey.interestOps(SelectionKey.OP_ACCEPT);
        //创建客户端
        NettyUtil.createClient(1);
        //建立selector和channel的连接;
        while (true){
            //没有感兴趣的事件过来就会阻塞,有感兴趣的事件过来就会继续运行;
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()){
                System.out.println("获取事件成功");
                SelectionKey eventKey = keyIterator.next();
                if (eventKey.isAcceptable()){
                    System.out.println("有客户端连接");
                    ServerSocketChannel channel = (ServerSocketChannel) eventKey.channel();
                    SocketChannel clientChannel = channel.accept();
                    clientChannel.configureBlocking(false);
                    //还需要把用户端注册到选择器上,并且设置好感兴趣的事件
                    System.out.println("客户端连接成功->添加事件");
                    clientChannel.register(selector, SelectionKey.OP_READ);
                }
                if (eventKey.isReadable()){
                    try{
                        System.out.println("有客户端读事件");
                        SocketChannel channel = (SocketChannel) eventKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        //channel.read()是往buffer里面写数据,tcp的挥手机制的,所以read返回-1表示客户端断开连接;
                        int read = channel.read(buffer);
                        if (read == -1){
                            System.out.println("客户端正常下线");
                            eventKey.cancel();
                            continue;
                        }
                        buffer.flip();
                        ByteBufferUtil.debugAll(buffer);
                    }catch (IOException e){
                        System.out.println("客户端断开连接");
                        eventKey.cancel();
                        continue;
                    }
                }
                if (eventKey.isWritable()){
                    System.out.println("有客户端写事件");
                }
                if (eventKey.isConnectable()){
                    System.out.println("有客户端连接事件");
                }
                keyIterator.remove();
            }
        }

    }
}
