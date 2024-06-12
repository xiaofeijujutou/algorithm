package com.xiaofei.algorithm.netty.test;

import com.xiaofei.algorithm.netty.util.ByteBufferUtil;
import com.xiaofei.algorithm.netty.util.NettyUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleServer {
    public static void main(String[] args) throws IOException {
        new BossEventLoop().register();
    }


    @Slf4j
    static class BossEventLoop implements Runnable {
        private static final int WORKER_THREAD_NUM = 2;
        private Selector bossSelector;
        private WorkerEventLoop[] workers;
        private volatile boolean start = false;
        AtomicInteger index = new AtomicInteger();

        public void register() throws IOException {
            if (!start) {
                //初始化服务器boss
                ServerSocketChannel ssc = NettyUtil.getNoBlockSSC();
                bossSelector = Selector.open();
                SelectionKey sscKey = ssc.register(bossSelector, SelectionKey.OP_ACCEPT, null);
                //初始化服务器worker
                workers = initEventLoops();
                //启动服务器
                new Thread(this, "boss").start();
                log.debug("boss start...");
                start = true;
            }
        }

        /**
         * 创建多个线程
         *
         * @return
         */
        public WorkerEventLoop[] initEventLoops() {
//        EventLoop[] eventLoops = new EventLoop[Runtime.getRuntime().availableProcessors()];
            WorkerEventLoop[] workerEventLoops = new WorkerEventLoop[WORKER_THREAD_NUM];
            for (int i = 0; i < workerEventLoops.length; i++) {
                workerEventLoops[i] = new WorkerEventLoop(i);
            }
            return workerEventLoops;
        }

        /**
         * 服务器的工作的就是不断接收请求,然后把channel注册到worker里面去
         * 自己不负责注册;
         */
        @Override
        public void run() {
            while (true) {
                try {
                    bossSelector.select();
                    Iterator<SelectionKey> iter = bossSelector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isAcceptable()) {
                            //接收用户端Channel;
                            ServerSocketChannel c = (ServerSocketChannel) key.channel();
                            SocketChannel sc = c.accept();
                            sc.configureBlocking(false);
                            log.debug("{} connected", sc.getRemoteAddress());
                            //接受到用户Channel之后使用负载均衡发送给worker
                            System.out.println("before register....");
                            workers[index.getAndIncrement() % workers.length].register(sc);
                            System.out.println("after register....");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Slf4j
    static class WorkerEventLoop implements Runnable {
        private Selector workerSelector;
        private volatile boolean start = false;//懒加载
        private int index;

        private final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

        /**
         * 懒加载,new的时候不创建线程,调用register的时候才创建;
         *
         * @param index
         */
        public WorkerEventLoop(int index) {
            this.index = index;
        }

        public void register(SocketChannel client) throws IOException {
            //懒加载,init;
            if (!start) {
                workerSelector = Selector.open();
                new Thread(this, "worker-" + index).start();
                start = true;
            }
            //提交任务,但是不会执行,这个是添加用户连接的任务;
            //程序运行到这里的时候是boss线程,不是worker线程,通过一个queue来交换数据,线程之间的通信;
            tasks.add(() -> {
                try {
                    SelectionKey clientKey = client.register(workerSelector, SelectionKey.OP_READ, null);
                    workerSelector.selectNow();
                    //假设这里还有很多业务逻辑要处理什么的...;
                    //使用异步通信就可以减少阻塞;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            //让select立即生效,也就是相当于打断一次阻塞,相当于通知另一个线程;
            workerSelector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    workerSelector.select();//阻塞 .wait
                    //查看一下有没有用户连接上来;
                    Runnable task = tasks.poll();
                    if (task != null) {
                        task.run();
                    }
                    Set<SelectionKey> keys = workerSelector.selectedKeys();
                    Iterator<SelectionKey> iter = keys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(128);
                            try {
                                int read = sc.read(buffer);
                                if (read == -1) {
                                    key.cancel();
                                    sc.close();
                                } else {
                                    buffer.flip();
                                    log.debug("{} message:", sc.getRemoteAddress());
                                    ByteBufferUtil.debugAll(buffer);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                key.cancel();
                                sc.close();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
