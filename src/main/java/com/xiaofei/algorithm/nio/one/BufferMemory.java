package com.xiaofei.algorithm.nio.one;

import java.nio.ByteBuffer;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/4/28 10:51
 */


public class BufferMemory {
    public static void main(String[] args) {
        //分配堆内存
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //分配非堆内存
        ByteBuffer bufferDirect = ByteBuffer.allocateDirect(1024);
    }
}
