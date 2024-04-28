package com.xiaofei.algorithm.bio.multiplechat;

import java.io.*;
import java.net.Socket;

public class ServerThreadReader extends Thread {
    private Socket socket;

    public ServerThreadReader(Socket socket) {
        this.socket = socket;
    }

    /**
     * 接收用户数据
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
            Server.receiveRegisterSocket.remove(socket);
            System.out.println("有人下线,在线人数为:" + Server.receiveRegisterSocket.size());
            System.out.println("接收方在线人数为:" + Server.sendRegisterSocket.size());
        }
    }

    private void sendMsgToAllClint(String msg) {
        for (Socket socket : Server.sendRegisterSocket) {
            try {
                PrintStream ps = new PrintStream(socket.getOutputStream());
                ps.println(msg);
                ps.flush();
            } catch (Exception e) {
                Server.sendRegisterSocket.remove(socket);
                System.out.println("接收方下线,剩余数量:" + Server.sendRegisterSocket.size());
            }
        }
    }
}
