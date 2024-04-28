package com.xiaofei.algorithm.bio.multiplechat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 用户的客户端
 */
public class Client {
    public static void main(String[] args) {
        try {
            //这边的发送方要发给对方的接收方
            Socket receive = new Socket("127.0.0.1", Server.SEND);
            Socket send = new Socket("127.0.0.1", Server.RECEIVE);
            //不断接收
            new Thread(()->{
                try {
                    InputStream is = receive.getInputStream();
                    BufferedReader bis = new BufferedReader(new InputStreamReader(is));
                    //这是用户,只能接收
                    String msg;
                    while ((msg = bis.readLine()) != null) {
                        System.out.println(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
            //不断发送
           new Thread(()->{
               try {
                   OutputStream os = send.getOutputStream();
                   PrintStream ps = new PrintStream(os);
                   Scanner scanner = new Scanner(System.in);
                   while (true){
                       ps.println(scanner.next());
                       ps.flush();
                   }
               } catch (Exception e) {
                   throw new RuntimeException(e);
               }
           }).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
