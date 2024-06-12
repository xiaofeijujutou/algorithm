package com.xiaofei.algorithm.netty.app.chat.protocal;

import com.xiaofei.algorithm.netty.app.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * ByteBuffer转换成对应的消息类型,也就是泛型里面的数据类型;
 * @create 2024/6/12 14:50
 */


public class MessageCodec extends ByteToMessageCodec<Message> {
    /**
     * 编码
     * @param channelHandlerContext
     * @param message
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        //1,写入魔数
        byteBuf.writeBytes(new byte[]{0x01, 0x02, 0x03, 0x04});
        //2,写入版本号
        byteBuf.writeByte(1);
        //3,写入序列化方式,jdk是0,json是1
        byteBuf.writeByte(0);
        //4,写入字节的指令类型
        byteBuf.writeByte(message.getMessageType());
        //5,写入请求序号,提高双工通信
        byteBuf.writeInt(message.getSequenceId());
        //填充
        byteBuf.writeByte(0xff);
        //6,获取写入字节数组,对象输出流要套一个字节数组输出流;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes = bos.toByteArray();
        //7,写入长度
        byteBuf.writeInt(bytes.length);
        //8,写入内容
        byteBuf.writeBytes(bytes);
    }

    /**
     * 解码
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //1,读取魔数
        Integer magicNum = byteBuf.readInt();
        //2,读取版本号
        Byte version = byteBuf.readByte();
        //3,读取序列化方式,jdk是0,json是1
        Byte serializerType = byteBuf.readByte();
        //4,读取字节的指令类型
        Byte messageType = byteBuf.readByte();
        //5,读取请求序号,提高双工通信
        Integer sequenceId = byteBuf.readInt();
        //读取填充
        byteBuf.readByte();
        //6,读取长度
        Integer length = byteBuf.readInt();
        //7,读取内容
        byte[] bytes = new byte[length];
        ByteBuf message = byteBuf.readBytes(bytes);
        Message messageObject = null;
        if (serializerType == 0){
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            messageObject = (Message) ois.readObject();
        }
        list.add(messageObject);

    }
}
