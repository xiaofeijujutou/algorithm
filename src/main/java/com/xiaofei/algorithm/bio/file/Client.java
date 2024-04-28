package com.xiaofei.algorithm.bio.file;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 用户的客户端
 */
public class Client {
    public static void main(String[] args) {
        try (
                InputStream is = new FileInputStream("D:\\desktop_d\\workspace\\algorithm\\src\\main\\webapp\\狗.png")
        ) {
            Socket socket = new Socket("127.0.0.1", 9999);
            //使用DataOutputStream可以分段发送数据,也就是说先发文件类型,再发文件;
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(".png");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                dos.write(buffer, 0, len);
            }
            dos.flush();
            //发送完之后要调用close或者shutdownOutPut告诉服务器端结束通信;
            socket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
