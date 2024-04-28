package com.xiaofei.algorithm.bio.thread;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 用户的客户端
 */
public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            //字节输出流可以写0101,但是这里为统一输出,封装成字节流
            OutputStream os = socket.getOutputStream();
            //PrintStream对应的就是InputStreamReader/BufferedReader
            PrintStream ps = new PrintStream(os);
            Scanner scanner = new Scanner(System.in);
            for (int i = 0; i < 20; i++) {
                ps.println(scanner.next());
                //flush可以看成是发送
                ps.flush();
            }
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
