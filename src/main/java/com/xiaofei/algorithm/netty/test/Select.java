package com.xiaofei.algorithm.netty.test;

import com.xiaofei.algorithm.netty.util.ByteBufferUtil;
import com.xiaofei.algorithm.netty.util.NettyUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

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
        System.out.println("服务器启动!!!");
        //创建客户端
        //NettyUtil.createClient(1);
        //建立selector和channel的连接;
        while (true) {
            //没有感兴趣的事件过来就会阻塞,有感兴趣的事件过来就会继续运行;
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey eventKey = keyIterator.next();
                keyIterator.remove();
                try {
                    System.out.println("获取事件成功");
                    if (eventKey.isAcceptable()) {
                        System.out.println("有客户端连接");
                        ServerSocketChannel channel = (ServerSocketChannel) eventKey.channel();
                        SocketChannel clientChannel = channel.accept();
                        clientChannel.configureBlocking(false);
                        //还需要把用户端注册到选择器上,并且设置好感兴趣的事件
                        System.out.println("客户端连接成功->添加事件");
                        ByteBuffer buffer = ByteBuffer.allocate(16);//读取的attachment
                        SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ, buffer);
                        //链接成功之后给客户端写点数据回去;
                        //sendsMsgToClient(30000000, clientChannel);

                        String msg = getMsg(30000000);
                        ByteBuffer writeBuffer = Charset.defaultCharset().encode(msg);
                        int write = clientChannel.write(writeBuffer);
                        System.out.println("发送一次,长度为: " + write);
                        if (writeBuffer.hasRemaining()){
                            //如果给客户端发送的还有剩余,就要给他添加写事件;
                            clientKey.interestOps(SelectionKey.OP_READ + SelectionKey.OP_WRITE);
                            clientKey.attach(writeBuffer);
                        }
                    }
                    if (eventKey.isReadable()) {
                        System.out.println("有客户端读事件");
                        SocketChannel channel = (SocketChannel) eventKey.channel();
                        //此时buffer的寿命就和eventKey寿命相同;
                        ByteBuffer buffer = (ByteBuffer) eventKey.attachment();
                        //channel.read()是往buffer里面写数据,tcp的挥手机制的,所以read返回-1表示客户端断开连接;
                        int read = channel.read(buffer);
                        if (read == -1) {
                            throw new IOException("用户正常下线");
                        } else {
                            if (buffer.position() == buffer.limit()) {
                                //扩容
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.limit() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                eventKey.attach(newBuffer);
                                ByteBufferUtil.copyRead(newBuffer);
                            } else {
                                buffer.flip();
                                ByteBufferUtil.copyRead(buffer);
                            }
                        }
                    }
                    if (eventKey.isWritable()) {
                        System.out.println("有客户端写事件");
                        SocketChannel client = (SocketChannel) eventKey.channel();
                        ByteBuffer writeBuffer = (ByteBuffer) eventKey.attachment();
                        int write = client.write(writeBuffer);
                        System.out.println("发送给用户数据,长度为: " + write);
                        //没有数据可以写了,就要取消关注这个事件
                        if (!writeBuffer.hasRemaining()){
                            //清理缓存,不然OOM;
                            eventKey.attach(null);
                            eventKey.interestOps(eventKey.interestOps() - SelectionKey.OP_WRITE);
                        }
                    }
                    if (eventKey.isConnectable()) {
                        System.out.println("有客户端连接事件");
                    }
                } catch (IOException e) {
                    if (e.getMessage().equals("用户正常下线")){
                        System.out.println("用户正常下线");
                    }else {
                        System.out.println("客户端断开连接");
                    }
                    eventKey.cancel();
                }
            }
        }

    }

    /**
     * 发消息给用户,这里相当于是阻塞式发送;
     *
     * @param socketChannel
     * @param msg
     * @throws IOException
     */
    private static void sendsMsgToClient(String msg, SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = Charset.defaultCharset().encode(msg);
        int total = 0;
        while (buffer.hasRemaining()) {
            int write = socketChannel.write(buffer);
            total += write;
            System.out.println("服务端发送了" + write + "大小的数据");
        }
        System.out.println("一共发送了" + total + "大小的数据");
    }

    private static void sendsMsgToClient(int data, SocketChannel socketChannel) throws IOException {
        sendsMsgToClient(getMsg(data), socketChannel);
    }
    private static String getMsg(int data)  {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data; i++) {
            sb.append(i);
        }
        return sb.toString();
    }
}
