package com.xiaofei.algorithm.bio.file;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * 服务器的服务端;
 */
public class Server {
    public static void main(String[] args) {
        try{
            System.out.println("====服务端启动====");
            ServerSocket ss = new ServerSocket(9999);
            Socket socket = ss.accept();
            //从客户端获取输入流数据;
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String suffix = dis.readUTF();
            System.out.println("服务器收到文件:" + suffix);
            //获取一个文件输出流
            OutputStream os = new FileOutputStream(
                    "D:\\desktop_d\\workspace\\algorithm\\src\\main\\java\\com\\xiaofei\\algorithm\\bio\\file\\file\\"
                            + UUID.randomUUID().toString() + suffix);
            //把用户发来的文件输入流的数据写到输出流里面去;
            byte[] buffer = new byte[1024];
            int len;
            while ((len = dis.read(buffer)) > 0){
                os.write(buffer);
            }
            System.out.println("服务器接受到文件保存成功");
            os.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
