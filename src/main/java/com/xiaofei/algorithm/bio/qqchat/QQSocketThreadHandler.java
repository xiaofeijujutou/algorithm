package com.xiaofei.algorithm.bio.qqchat;

import com.xiaofei.algorithm.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Set;

public class QQSocketThreadHandler extends Thread{
    private Socket socket;
    public QQSocketThreadHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //使用DataInputStream的好处是:可以方便的切换多种数据的传输;
        DataInputStream dis = null;
        try{
            //获取socket的输入流
            dis = new DataInputStream(socket.getInputStream());
            while (true){
                //约定好第一次发送的格式,如果是1就走登陆;
                int flag = dis.readInt();
                if (flag == Constants.LOGIN){
                    //登陆的第二次发送就是发送用户名字
                    String userName = dis.readUTF();
                    System.out.println("用户: " + userName + "已经上线,地址为: " + socket.getRemoteSocketAddress());
                    ServerChat.onLineSocket.put(socket, userName);
                }
                writeMsg(flag, dis);
            }
        }catch (Exception e){
            System.out.println("用户: " + ServerChat.onLineSocket.get(socket) + "已经下线");
            ServerChat.onLineSocket.remove(socket);
            try {
                writeMsg(Constants.LOGIN, dis);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void writeMsg(int flag, DataInputStream dis) throws Exception{
        String msg = null;
        if (flag == Constants.LOGIN){
            //如果是有人登陆,就要更新所有人的在线情况
            StringBuilder sb = new StringBuilder();
            //用集合类<String>直接获取values
            Collection<String> onLineUser = ServerChat.onLineSocket.values();
            if (onLineUser != null && onLineUser.size() > 0){
                for (String name : onLineUser) {
                    sb.append(name + Constants.SPLIT);
                }
                msg = sb.substring(0, sb.lastIndexOf(Constants.SPLIT));
                sendMsgToAll(flag, msg);
            }
        }
        //群发或者@某人
        else if(flag == Constants.MULTIPLY || flag == Constants.AT){
            //获取消息
            String newMsg = dis.readUTF();
            //获取发件人
            String sender = ServerChat.onLineSocket.get(socket);
            //时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");
            //最终发出去的消息
            StringBuilder finalMsg = new StringBuilder().append(sender)
                    .append("    ")
                    .append(sdf.format(System.currentTimeMillis()))
                    .append(flag == Constants.AT ? "对你私发" : "")
                    .append("\r\n")
                    .append("    ")
                    .append(newMsg)
                    .append("\r\n");
            if (flag == Constants.MULTIPLY){
                sendMsgToAll(flag, finalMsg.toString());
            }else if (flag == Constants.AT){
                String destName = dis.readUTF();
                sendMsgToOne(destName, finalMsg.toString());
            }
        }
    }

    /**
     * 发消息给指定的人
     * @param destName 名字
     * @param msg 内容
     */
    private void sendMsgToOne(String destName, String msg) throws Exception{
        Set<Socket> allOnLineSocket = ServerChat.onLineSocket.keySet();
        for (Socket s : allOnLineSocket) {
            if (ServerChat.onLineSocket.get(s).equals(destName.trim())){
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(Constants.MULTIPLY);
                dos.writeUTF(msg);
                dos.flush();
                DataOutputStream local = new DataOutputStream(socket.getOutputStream());
                local.writeInt(Constants.MULTIPLY);
                local.writeUTF(msg);
                local.flush();
            }
        }
    }


    /**
     * 把用户a登陆上线的信息推送给所有人
     * @param flag
     * @param msg
     * @throws IOException
     */
    private void sendMsgToAll(int flag, String msg) throws IOException {
        Set<Socket> allOnLineSocket = ServerChat.onLineSocket.keySet();
        for (Socket s : allOnLineSocket) {
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeInt(flag);
            dos.flush();
            dos.writeUTF(msg);
            dos.flush();
        }
    }
}