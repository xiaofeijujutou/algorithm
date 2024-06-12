package com.xiaofei.algorithm.netty.app.chat.protocal;

import com.xiaofei.algorithm.netty.app.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/6/12 17:29
 */


public class TestMessageCodec {
    public static void main(String[] args) throws Exception{
        EmbeddedChannel channel = new EmbeddedChannel(//   最大1024字节，长度域偏移为12也就是偏移12之后找到长度，长度域长度为4，长度域偏移为0，长度域长度为0不去除前面部分;
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new LoggingHandler(),
                new MessageCodec());
        // encode把对象转换01
        channel.writeOutbound(new LoginRequestMessage("zhangsan", "123"));
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        MessageCodec messageCodec = new MessageCodec();
        ArrayList<Object> objects = new ArrayList<>();
        messageCodec.encode(null, new LoginRequestMessage("zhangsan", "123"), buffer);
        messageCodec.decode(null, buffer, objects);
        System.out.println(objects.get(0));

    }
}
