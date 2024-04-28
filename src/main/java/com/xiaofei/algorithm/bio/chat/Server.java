package com.xiaofei.algorithm.bio.chat;



import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 需要实现的需求:
 *  1,注册端口;
 *  2,接收socket,交给线程处理;
 *  3,把当前连接
 */
public class Server{

    public static List<Socket> registerSocket = new ArrayList<>();
    public static void main(String[] args) {
        try{
            System.out.println("====服务端启动====");
            //ServerSocket可以是单例的,会一直监听端口
            ServerSocket ss = new ServerSocket(9999);
            while (true){
                //链接多个socket,交给线程执行
                Socket socket = ss.accept();
                registerSocket.add(socket);
                new ServerThreadReader(socket).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
