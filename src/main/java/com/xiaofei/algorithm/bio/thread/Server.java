package com.xiaofei.algorithm.bio.thread;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器的服务端
 * 目标:实现接收多个客户端的socket请求,每一个socket都交给一个线程;
 */
public class Server {
    public static void main(String[] args) {
        try{
            System.out.println("====服务端启动====");
            //ServerSocket可以是单例的,会一直监听端口
            ServerSocket ss = new ServerSocket(9999);
            while (true){
                //链接多个socket,交给线程执行
                Socket socket = ss.accept();
                new ServerThreadReader(socket).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
