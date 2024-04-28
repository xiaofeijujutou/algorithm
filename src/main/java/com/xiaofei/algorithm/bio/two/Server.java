package com.xiaofei.algorithm.bio.two;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器的服务端;
 */
public class Server {
    public static void main(String[] args) {
        try{
            System.out.println("====服务端启动====");
            //ServerSocket可以是单例的,会一直监听端口
            ServerSocket ss = new ServerSocket(9999);
            //ss监听到请求之后,就会吧请求封装成socket;
            //Socket通行,客户端的Socket断了,服务端也会断;
            Socket socket = ss.accept();
            //从客户端获取数据;
            InputStream is = socket.getInputStream();
            //封装成缓冲字符输入流(如果发送的是数据,就用字符,如果是文件图片,就用字节);
            BufferedReader bis = new BufferedReader(new InputStreamReader(is));
            String msg;
            //一直阻塞
            while ((msg = bis.readLine()) != null){
                System.out.println(msg);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
