package com.xiaofei.algorithm.bio.chat;

import java.io.*;
import java.net.Socket;

public class ServerThreadReader extends Thread {
    private Socket socket;

    public ServerThreadReader(Socket socket) {
        this.socket = socket;
    }

    /**
     * socket是双向通信;
     *      client使用output发送消息,server使用input接收;
     *      server接收到了之后,调用别的socket的output发消息;
     * socket可以实现双向通信,一对一的话,来回收发都要提前写好代码逻辑;
     */
    @Override
    public void run() {
        try {
            //从客户端获取数据;
            InputStream is = socket.getInputStream();
            //从socket里面拿到input输入流,从is里面获取数据
            BufferedReader bis = new BufferedReader(new InputStreamReader(is));
            //获取到数据,转发给所有socket
            String msg;
            while ((msg = bis.readLine()) != null) {
                sendMsgToAllClint(msg);
            }
        } catch (IOException e) {
            System.out.println("有人下线");
            Server.registerSocket.remove(this.socket);
        }
    }

    private void sendMsgToAllClint(String msg) {
        try {
            for (Socket socket : Server.registerSocket) {
                if(socket != this.socket){
                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    ps.println(msg);
                    ps.flush();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
