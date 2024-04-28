package com.xiaofei.algorithm.bio.qqchat;


import com.xiaofei.algorithm.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 真正的服务器
 * 使用一个线程来完成收发:
 *      使用DataStream,约定好发送协议;
 */
public class ServerChat {
    public static Map<Socket, String> onLineSocket = new HashMap<>();
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");
        new Thread(()->{
            while (true){
                System.out.println("当前用户在线数量:" + onLineSocket.size());
                System.out.println("当前时间: " + sdf.format(System.currentTimeMillis()));
                Collection<String> strings = onLineSocket.values();
                for (String string : strings) {
                    System.out.println(string);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();


        try {
            //注册端口
            ServerSocket ss = new ServerSocket(Constants.PORT);
            while (true){
                //监听到一个请求就开启一个线程
                Socket socket = ss.accept();
                new QQSocketThreadHandler(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
