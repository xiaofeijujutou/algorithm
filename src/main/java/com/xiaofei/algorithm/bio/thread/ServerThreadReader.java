package com.xiaofei.algorithm.bio.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThreadReader extends Thread {
    private Socket socket;

    public ServerThreadReader(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            //从客户端获取数据;
            InputStream is = socket.getInputStream();
            //封装成缓冲字符输入流(如果发送的是数据,就用字符,如果是文件图片,就用字节);
            BufferedReader bis = new BufferedReader(new InputStreamReader(is));
            String msg;
            //一直阻塞
            while ((msg = bis.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
