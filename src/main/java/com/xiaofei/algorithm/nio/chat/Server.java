package com.xiaofei.algorithm.nio.chat;

import com.xiaofei.algorithm.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/4/30 14:57
 */


public class Server {
    private ServerSocketChannel ssChannel;
    private Selector selector;

    public Server() {
        try {
            selector = Selector.open();
            ssChannel = ServerSocketChannel.open();
            //ssChannel绑定端口;
            ssChannel.bind(new InetSocketAddress(Constants.PORT));
            ssChannel.configureBlocking(false);
            //selector注册ServerSocket.accept()方法;
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("------服务器启动------");
        } catch (Exception e) {

        }
    }

    public void listen() {
        try {
            //监听8080端口,监听ssChannel的accept()方法,但是只是监听,不是实现;
            while (selector.select() > 0) {
                //接受到事件,获取事件,从事件里面可以获取事件对应的通道;
                Iterator<SelectionKey> events = selector.selectedKeys().iterator();
                while (events.hasNext()) {
                    SelectionKey event = events.next();
                    //注册事件,这个event是ServerSocketChannel;
                    if (event.isAcceptable()) {
                        //实现ssChannel.accept()
                        SocketChannel ClientSocket = ssChannel.accept();
                        System.out.println("接收到用户上线,ip: " + ClientSocket.getRemoteAddress());
                        ClientSocket.configureBlocking(false);
                        //将用户的读取操作注册到选择器;
                        ClientSocket.register(selector, SelectionKey.OP_READ);
                        //将用户的写入操作注册到选择器;
                        //ClientSocket.register(selector, SelectionKey.OP_WRITE);
                    }
                    //用户已经连接成功,发送过来了要写入的数据;
                    else if (event.isReadable()) {
                        System.out.println("用户发送数据");
                        //实现群聊,需要转发给其他用户;
                        getClientData(event);
                    }
                    events.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前客户端的消息,然后转发给其他全部客户端;
     *
     * @param event
     */
    private void getClientData(SelectionKey event) {
        SocketChannel clientChannel = null;
        try {
            clientChannel = (SocketChannel) event.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = clientChannel.read(buffer)) > 0) {
                buffer.flip();
                sb.append(new String(buffer.array(), 0, buffer.remaining()));
                buffer.clear();
            }
            System.out.println("客户端接收到数据:" + sb.toString());
            sendMsgToAllClint(sb.toString(), clientChannel);
        } catch (Exception e) {
            //客户端可能下线,那就把这个事件给取消掉;
            try {
                System.out.println("有人下线" + clientChannel.getRemoteAddress());
                event.channel();
                clientChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 把当前客户端的消息转发给当前全部在线注册的channel;
     *
     * @param msg
     * @param clientChannel
     */
    private void sendMsgToAllClint(String msg, SocketChannel clientChannel) throws IOException {
        System.out.println("服务端开始转发消息");
        //选择器selector.selectedKeys()是已经就绪的事件,selector.keys是所有事件,不管就绪还是没就绪;
        for (SelectionKey event : selector.keys()) {
            //这里的通道有一个是ServerSocketChannel的通道,也是注册到了选择器上;
            Channel channel = event.channel();
            if (channel instanceof SocketChannel) {
                ((SocketChannel)channel).write(ByteBuffer.wrap(msg.getBytes()));

            }
        }
    }


}
