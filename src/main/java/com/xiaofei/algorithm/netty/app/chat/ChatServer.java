package com.xiaofei.algorithm.netty.app.chat;



import com.xiaofei.algorithm.Constants;
import com.xiaofei.algorithm.netty.app.chat.protocal.MessageCodecSharable;
import com.xiaofei.algorithm.netty.app.chat.protocal.ProtocolFrameDecoder;
import com.xiaofei.algorithm.netty.app.chat.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        System.out.println("服务器启动!");
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //initChannel是刚刚建立链接的时候触发的请求
                    //添加handle,先处理黏包半包,拆分好ByteBuf给下一个
                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    //日志
//                    ch.pipeline().addLast(LOGGING_HANDLER);
                    //从处理好黏包的处理器后拿到完整数据,然后执行解码操作,序列化成对象
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    //这个SimpleHandle可以只处理泛型对应的数据类型,其他的就会被忽略;
                    ch.pipeline().addLast(new LoginRequestMessageHandler());//登录,然后绑定
                    ch.pipeline().addLast(new ChatRequestMessageHandler());//发送消息;
                    ch.pipeline().addLast(new GroupChatRequestMessageHandler());
                    ch.pipeline().addLast(new GroupCreateRequestMessageHandler());
                    ch.pipeline().addLast(new GroupJoinRequestMessageHandler());
                    ch.pipeline().addLast(new GroupQuitRequestMessageHandler());
                    ch.pipeline().addLast(new GroupMembersRequestMessageHandler());
                    ch.pipeline().addLast(new QuitHandler());
                }
            });
            Channel channel = serverBootstrap.bind(Constants.PORT).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
// ch.pipeline().addLast(LOGIN_HANDLER);
//                    ch.pipeline().addLast(CHAT_HANDLER);
//                    ch.pipeline().addLast(GROUP_CREATE_HANDLER);
//                    ch.pipeline().addLast(GROUP_JOIN_HANDLER);
//                    ch.pipeline().addLast(GROUP_MEMBERS_HANDLER);
//                    ch.pipeline().addLast(GROUP_QUIT_HANDLER);
//                    ch.pipeline().addLast(GROUP_CHAT_HANDLER);
//ch.pipeline().addLast(QUIT_HANDLER);


//        LoginRequestMessageHandler LOGIN_HANDLER = new LoginRequestMessageHandler();
//        ChatRequestMessageHandler CHAT_HANDLER = new ChatRequestMessageHandler();
//        GroupCreateRequestMessageHandler GROUP_CREATE_HANDLER = new GroupCreateRequestMessageHandler();
//        GroupJoinRequestMessageHandler GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();
//        GroupMembersRequestMessageHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestMessageHandler();
//        GroupQuitRequestMessageHandler GROUP_QUIT_HANDLER = new GroupQuitRequestMessageHandler();
//        GroupChatRequestMessageHandler GROUP_CHAT_HANDLER = new GroupChatRequestMessageHandler();
// QuitHandler QUIT_HANDLER = new QuitHandler();


// // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
//                    // 5s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件
//                    ch.pipeline().addLast(new IdleStateHandler(5, 0, 0));
//                    // ChannelDuplexHandler 可以同时作为入站和出站处理器
//                    ch.pipeline().addLast(new ChannelDuplexHandler() {
//                        // 用来触发特殊事件
//                        @Override
//                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
//                            IdleStateEvent event = (IdleStateEvent) evt;
//                            // 触发了读空闲事件
//                            if (event.state() == IdleState.READER_IDLE) {
//                                log.debug("已经 5s 没有读到数据了");
//                                ctx.channel().close();
//                            }
//                        }
//                    });