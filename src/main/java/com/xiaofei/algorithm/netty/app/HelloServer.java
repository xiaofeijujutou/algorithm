package com.xiaofei.algorithm.netty.app;

import com.xiaofei.algorithm.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/6/5 16:51
 */


public class HelloServer {
    public static void main(String[] args) {
        System.out.println("服务器启动");
        // 服务器的启动器,组装netty组件,启动服务器
        new ServerBootstrap()
                //EventLoopGroup和之前写的nio代码差不多,包含了一个selector和一个thread,group就是组,又有boss又有worker
                .group(new NioEventLoopGroup())
                //支持nio,bio
                .channel(NioServerSocketChannel.class)
                //上面创建了WorkerGroup,worker具体需要做什么,就是下面的childHandler决定
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    //这个channel就是和用户通信的channel,里面写的代码就是初始化要执行的事情,在连接建立后被调用;
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //netty传输的数据都是字节形式,这里将ByteBuf转为String;
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            //channel触发了Read事件,在数据被发送过来后,会先被decoder成字符串,然后传给channelRead方法
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(Constants.PORT);
    }
}
