package com.xiaofei.algorithm.bio.multiplechat;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server{
    public static final int SEND = 10022;
    public static final int RECEIVE = 19992;
    public static List<Socket> receiveRegisterSocket = new ArrayList<>();
    public static List<Socket> sendRegisterSocket = new ArrayList<>();
    public static void main(String[] args) {
        try{
            System.out.println("====服务端启动====");
            //接收所有用户信息
            ServerSocket receive = new ServerSocket(RECEIVE);
            //发消息给用户
            ServerSocket send = new ServerSocket(SEND);
            new Thread(()->{
                while (true){
                    try {
                        Socket socket = receive.accept();
                        receiveRegisterSocket.add(socket);
                        System.out.println("receive服务器有人注册,在线人数:" + receiveRegisterSocket.size());
                        new ServerThreadReader(socket).start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
            new Thread(()->{
                while (true){
                    try {
                        Socket socket = send.accept();
                        sendRegisterSocket.add(socket);
                        System.out.println("send服务器有人注册,在线人数:" + sendRegisterSocket.size());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
