package com.xiaofei.algorithm.bio.threadpool;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器的服务端
 * 目标:实现伪异步bio功能的实现;
 */
public class Server {

    private static HandlerSocketServerPool pool = new HandlerSocketServerPool(5,3);
    public static void main(String[] args) {
        try{
            System.out.println("====服务端启动====");
            //ServerSocket可以是单例的,会一直监听端口
            ServerSocket ss = new ServerSocket(9999);
            while (true){
                //链接多个socket,交给线程执行
                Socket socket = ss.accept();
                Runnable target = new ServerRunnableTarget(socket);
                pool.execute(target);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
