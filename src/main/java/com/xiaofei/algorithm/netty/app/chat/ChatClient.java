package com.xiaofei.algorithm.netty.app.chat;

import com.xiaofei.algorithm.Constants;
import com.xiaofei.algorithm.netty.app.chat.handler.ClientChannelInboundHandlerAdapter;
import com.xiaofei.algorithm.netty.app.chat.protocal.MessageCodecSharable;
import com.xiaofei.algorithm.netty.app.chat.protocal.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/6/5 17:26
 */


public class ChatClient {

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("客户端启动");
            LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
            MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
            //启动客户端
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(new NioEventLoopGroup());
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    //日志
//                    ch.pipeline().addLast(LOGGING_HANDLER);
                    //从处理好黏包的处理器后拿到完整数据,然后执行解码操作,序列化成对象
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    //这个才是和之前nio联系的Handle,绑定各种事件就需要重写各种方法;
                    ch.pipeline().addLast("client handle", new ClientChannelInboundHandlerAdapter());
                }
            });
            Channel channel = bootstrap.connect(Constants.LOCALHOST, Constants.PORT).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
