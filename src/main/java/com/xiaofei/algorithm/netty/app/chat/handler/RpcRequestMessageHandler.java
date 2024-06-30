package com.xiaofei.algorithm.netty.app.chat.handler;

import com.xiaofei.algorithm.netty.app.chat.RpcClient;
import com.xiaofei.algorithm.netty.app.chat.protocal.SequenceIdGenerator;
import com.xiaofei.algorithm.netty.app.chat.service.HelloService;
import com.xiaofei.algorithm.netty.app.chat.service.ServicesFactory;
import com.xiaofei.algorithm.netty.app.chat.message.*;
import com.xiaofei.algorithm.netty.test.Select;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    /**
     * 走到这里来了,消息类型肯定是RPC请求,而且上面指明了对RPC请求感兴趣
     *
     * @param ctx
     * @param message
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) {
        RpcResponseMessage response = new RpcResponseMessage();
        response.setSequenceId(message.getSequenceId());
        try {
            //通过反射调用服务,返回结果或者异常;
            HelloService service = (HelloService)
                    ServicesFactory.getService(Class.forName(message.getInterfaceName()));
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
            Object invoke = method.invoke(service, message.getParameterValue());
            response.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getCause().getMessage();
            response.setExceptionValue(new Exception("远程调用出错:" + msg));
        }
        ctx.writeAndFlush(response);
    }

    public static <T> T getProxyService(Class<T> interfaceClass) {
        ClassLoader loader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{interfaceClass};
        Object o = Proxy.newProxyInstance(loader,
                interfaces,
                (proxy, method, args) -> {
                    //1.创建消息对象
                    RpcRequestMessage message = new RpcRequestMessage(
                            SequenceIdGenerator.nextId(),
                            interfaceClass.getName(),
                            method.getName(),
                            method.getReturnType(),
                            method.getParameterTypes(),
                            args
                    );
                    //发送消息
                    Channel channel = RpcClient.getChannel();
                    channel.writeAndFlush(message);
                    //保存请求id
                    DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
                    RpcResponseMessageHandler.PROMISE.put(message.getSequenceId(), promise);
                    //进入等待,等服务端写回数据,然后往promise塞入数据,然后return;
                    promise.await();
                    if (promise.isSuccess()){
                        return promise.getNow();
                    }else {
                        throw new RuntimeException(promise.cause());
                    }
                });
        return (T) o;
    }

    public static void main(String[] args) {
        HelloService service = getProxyService(HelloService.class);
        service.sayHello("张三");
    }
}
