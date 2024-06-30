package com.xiaofei.algorithm.netty.app.chat.handler;

import com.xiaofei.algorithm.netty.app.chat.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/6/19 16:22
 */


public class ClientChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    static Scanner scanner = new Scanner(System.in);
    CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
    //用来保存登录状态
    AtomicBoolean LOGIN = new AtomicBoolean(false);
    //连接建立之后会触发这个channelActive事件;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            System.out.println("请输入用户名:");
            String userName = scanner.next();
            System.out.println("请输入密码:");
            String password = scanner.next();
            LoginRequestMessage message = new LoginRequestMessage(userName, password);
            //这是入栈处理器,调用ctx.writeAndFlush(message)就会从下往上调用出栈处理器;
            //相当于是连接成功,触发channelActive事件,然后把消息写回给服务器;
            ctx.writeAndFlush(message);
            System.out.println("等待登录结果...");
            try {
                WAIT_FOR_LOGIN.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //登录拿到结果之后被唤醒
            if (!LOGIN.get()) {
                System.out.println("登录失败");
                ctx.channel().close();
                return;
            }
            //执行一些用户操作
            doUserOperator(ctx, userName);
        }, "scanner").start();
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof LoginResponseMessage) {
            LoginResponseMessage message = (LoginResponseMessage) msg;
            LOGIN.set(message.isSuccess());
            WAIT_FOR_LOGIN.countDown();
        }
        else if (msg instanceof ChatResponseMessage){
            ChatResponseMessage message = (ChatResponseMessage) msg;
            System.out.println(message.getFrom() + ":" + message.getContent());
        }else {
            System.out.println("客户端收到消息:" + msg);
            super.channelRead(ctx, msg);
        }
    }

    private static void doUserOperator(ChannelHandlerContext ctx, String userName) {
        while (true) {
            System.out.println("============================");
            System.out.println("send [username] [content]");
            System.out.println("gsend [group name] [content]");
            System.out.println("gcreate [group name] [m1,m2,m3...]");
            System.out.println("gmembers [group name]");
            System.out.println("gjoin [group name]");
            System.out.println("gquit [group name]");
            System.out.println("quit");
            System.out.println("============================");
            String command = scanner.nextLine();
            String[] mand = command.split(" ");
            switch (mand[0]) {
                case "send":
                    ctx.writeAndFlush(new ChatRequestMessage(userName, mand[1], mand[2]));
                    break;
                case "gsend":
                    ctx.writeAndFlush(new GroupChatRequestMessage(userName, mand[1], mand[2]));
                    break;
                case "gcreate":
                    HashSet<String> groupNames = new HashSet<>(Arrays.asList(mand[2].split(",")));
                    groupNames.add(userName);
                    ctx.writeAndFlush(new GroupCreateRequestMessage(mand[1], groupNames));
                    break;
                case "gmembers":
                    ctx.writeAndFlush(new GroupMembersRequestMessage(mand[1]));
                    break;
                case "gjoin":
                    ctx.writeAndFlush(new GroupJoinRequestMessage(userName, mand[1]));
                    break;
                case "gquit":
                    ctx.writeAndFlush(new GroupQuitRequestMessage(userName, mand[1]));
                    break;
                case "quit":
                    ctx.channel().close();
                    return;
                default:
                    System.out.println("无法识别命令");
            }
        }
    }
}
