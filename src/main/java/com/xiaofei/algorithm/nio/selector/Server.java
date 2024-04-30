package com.xiaofei.algorithm.nio.selector;

import com.xiaofei.algorithm.Constants;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/4/30 11:22
 */

/**
 * 目标：NIO非阻塞通信下的入门案例：服务端开发
 */
public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("---服务端启动---");
        //1.获取通道,原来叫ServerSocket,现在是通道,这个通道也是可以接收请求(事件)的,之前的不能
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //2.切换为非阻塞模式
        ssChannel.configureBlocking(false);
        //3.绑定连接的端口
        ssChannel.bind(new InetSocketAddress(Constants.PORT));
        //4.获取选择,创建选择器;
        Selector selector = Selector.open();
        //5.将通道都注册到选择器上去，并且开始指定监听接收事件,就是说ServerSocket接收到的所有连接都注册到Selector上
        //select监听ServerSocket的事件,方法是select(),用户发送任意请求都会被监听,然后selector就会往下走;
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6.使用Selector选择器轮询已经就绪好的事件
        //阻塞住,也就是说selector.select()会定时检测有没有事件;
        while (selector.select() > 0) {
            System.out.print("客户端发送了一个事件\t");
            //7.获取选择器中的所有注册的通道中已经就序好的事件
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            //8.开始遍历这些准备好的事件
            while (it.hasNext()) {
                //提取当前这个事件
                SelectionKey sk = it.next();
                //9.判断这个事件具体是什么事件
                //isAcceptable是注册,也就是将用户的socket注册到选择器上;
                if (sk.isAcceptable()) {
                    System.out.println("客户端事件为注册");
                    //10.直接获取当前接入的客户端通道
                    SocketChannel schannel = ssChannel.accept();
                    //11.将客户端通道也设置为非阻塞式的
                    schannel.configureBlocking(false);
                    //12.将客户端通道也注册到选择器Selector上
                    schannel.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    System.out.println("客户端事件为发送消息");
                    //13.获取当前选择器上的”读就绪事件“,就把客户端发送过来的消息打印出来;
                    SocketChannel sChannel = (SocketChannel) sk.channel();
                    //14.开始读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = sChannel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();//”清除“之前的数据
                    }
                }
                //处理完毕当前事件后，需要移除掉当前事件.否则会重复处理
                //客户端发送消息过来就是一个迭代器,客户端那边添加迭代器,这边需要移除;
                it.remove();
            }
        }
    }
}

