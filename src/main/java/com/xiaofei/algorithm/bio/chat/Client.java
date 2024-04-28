package com.xiaofei.algorithm.bio.chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 用户的客户端
 */
public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            InputStream is = socket.getInputStream();
            BufferedReader bis = new BufferedReader(new InputStreamReader(is));
            //这是用户,只能接收
            String msg;
            while ((msg = bis.readLine()) != null) {
                System.out.println(msg);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
