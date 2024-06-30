package com.xiaofei.algorithm.netty.app.chat.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Message implements Serializable {

    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();


    /**
     * 消息类型,子类实现了这个方法后会应用这个数据的其中一个;
     */
    //登录请求消息
    public static final int LoginRequestMessage = 0;
    //登录响应消息
    public static final int LoginResponseMessage = 1;
    //聊天请求消息
    public static final int ChatRequestMessage = 2;
    //聊天响应消息
    public static final int ChatResponseMessage = 3;
    //创建群聊请求消息
    public static final int GroupCreateRequestMessage = 4;
    //创建群聊响应消息
    public static final int GroupCreateResponseMessage = 5;
    //加入群聊请求消息
    public static final int GroupJoinRequestMessage = 6;
    //加入群聊响应消息
    public static final int GroupJoinResponseMessage = 7;
    //退出群聊请求消息
    public static final int GroupQuitRequestMessage = 8;
    //退出群聊响应消息
    public static final int GroupQuitResponseMessage = 9;
    //群聊请求消息
    public static final int GroupChatRequestMessage = 10;
    //群聊响应消息
    public static final int GroupChatResponseMessage = 11;
    //获取群聊成员请求消息
    public static final int GroupMembersRequestMessage = 12;
    //获取群聊成员响应消息
    public static final int GroupMembersResponseMessage = 13;
    //心跳请求消息
    public static final int PingMessage = 14;
    //心跳响应消息
    public static final int PongMessage = 15;
    /**
     * 请求类型 byte 值
     */
    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;
    /**
     * 响应类型 byte 值
     */
    public static final int  RPC_MESSAGE_TYPE_RESPONSE = 102;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(LoginRequestMessage, LoginRequestMessage.class);
        messageClasses.put(LoginResponseMessage, LoginResponseMessage.class);
        messageClasses.put(ChatRequestMessage, ChatRequestMessage.class);
        messageClasses.put(ChatResponseMessage, ChatResponseMessage.class);
        messageClasses.put(GroupCreateRequestMessage, GroupCreateRequestMessage.class);
        messageClasses.put(GroupCreateResponseMessage, GroupCreateResponseMessage.class);
        messageClasses.put(GroupJoinRequestMessage, GroupJoinRequestMessage.class);
        messageClasses.put(GroupJoinResponseMessage, GroupJoinResponseMessage.class);
        messageClasses.put(GroupQuitRequestMessage, GroupQuitRequestMessage.class);
        messageClasses.put(GroupQuitResponseMessage, GroupQuitResponseMessage.class);
        messageClasses.put(GroupChatRequestMessage, GroupChatRequestMessage.class);
        messageClasses.put(GroupChatResponseMessage, GroupChatResponseMessage.class);
        messageClasses.put(GroupMembersRequestMessage, GroupMembersRequestMessage.class);
        messageClasses.put(GroupMembersResponseMessage, GroupMembersResponseMessage.class);
        messageClasses.put(RPC_MESSAGE_TYPE_REQUEST, RpcRequestMessage.class);
        messageClasses.put(RPC_MESSAGE_TYPE_RESPONSE, RpcResponseMessage.class);
    }

}
