package com.xiaofei.algorithm.bio.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用来处理多链接的线程池
 */
public class HandlerSocketServerPool {
    private ExecutorService executorService;

    public HandlerSocketServerPool(int maxThreadNum, int queueSize) {
        this.executorService = new ThreadPoolExecutor(maxThreadNum,
                maxThreadNum,
                120,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable target){
        executorService.execute(target);
    }
}
