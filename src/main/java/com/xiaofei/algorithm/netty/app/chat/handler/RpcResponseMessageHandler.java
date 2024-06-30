package com.xiaofei.algorithm.netty.app.chat.handler;

import com.xiaofei.algorithm.netty.app.chat.message.RpcRequestMessage;
import com.xiaofei.algorithm.netty.app.chat.message.RpcResponseMessage;
import com.xiaofei.algorithm.netty.app.chat.service.HelloService;
import com.xiaofei.algorithm.netty.app.chat.service.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/6/29 14:03
 */


@Slf4j
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    //请求id(也可以用来链路追踪)
    public static final Map<Integer, Promise<Object>> PROMISE = new ConcurrentHashMap<>();

    /**
     * 这里是接收服务器的数据;
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        Promise<Object> promise = PROMISE.remove(msg.getSequenceId());
        if (promise != null){
            Object returnValue = msg.getReturnValue();
            Exception exceptionValue = msg.getExceptionValue();
            //set数据后就会调用notify方法唤醒另一边等待线程;
            if (exceptionValue != null){
                promise.setFailure(exceptionValue);
            }else {
                promise.setSuccess(returnValue);
            }
        }
        log.debug("{}", msg);
    }
}
