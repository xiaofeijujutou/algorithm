package com.xiaofei.algorithm.bio.qqchat;

import com.xiaofei.algorithm.Constants;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientReader extends Thread {
    private Socket socket;
    private ClientChat clientChat;
    public ClientReader(ClientChat clientChat, Socket socket) {
        this.clientChat = clientChat;
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            /** 循环一直等待客户端的消息 */
            while (true){
                /** 读取当前的消息类型： 登录，群发，私聊，@消息 */
                int flag = dis.readInt();
                if(flag == 1){
                    //在线人数消息回来了
                    String nameDatas =dis.readUTF();
                    System.out.println(nameDatas);
                    //展示到在线人数的界面
                    String[] names = nameDatas.split(Constants.SPLIT);
                    System.out.println(Arrays.toString(names));
                    clientChat.onLineUsers.setListData(names);
                }else if(flag == 2 ){
                    //群发，私聊 ，@消息 都是直接显示的
                    String msg = dis.readUTF();
                    clientChat.smsContent.append(msg);
                    //让消息界面滚动到低端
                    clientChat.smsContent.setCaretPosition(clientChat.smsContent.getText().length()
                    );
                }else if (flag == 3){
                    String msg = dis.readUTF();
                    clientChat.smsContent.append(msg);
                    clientChat.smsContent.setCaretPosition(clientChat.smsContent.getText().length());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

