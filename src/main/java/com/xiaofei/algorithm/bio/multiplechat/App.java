package com.xiaofei.algorithm.bio.multiplechat;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 小程序需要推送的客户端
 */
public class App {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            OutputStream os = socket.getOutputStream();
            PrintStream ps = new PrintStream(os);
            Scanner scanner = new Scanner(System.in);
            while (true){
                System.out.print("输入:");
                ps.println(scanner.next());
                ps.flush();
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
