package com.xiaofei.algorithm.nio.one;

import java.nio.ByteBuffer;

public class BufferTest {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println("position" + buffer.position());//0
        System.out.println("最大操作索引" + buffer.limit());//10
        System.out.println("capacity" + buffer.capacity());//10
        System.out.println("=======================");
        String name = "chouju";
        System.out.println(name.getBytes().length);
        buffer.put(name.getBytes());
        System.out.println("position" + buffer.position());
        System.out.println("最大操作索引" + buffer.limit());
        System.out.println("capacity" + buffer.capacity());
        System.out.println("=======================");
        buffer.flip();//切换成读模式之后,最大可操作的索引就是数据的长度而不是原始长度;
        System.out.println("position" + buffer.position());
        System.out.println("最大操作索引" + buffer.limit());
        System.out.println("capacity" + buffer.capacity());
        //读取一个指针就向后走一个,然后清空一个
        for (int i = 0; i < 4; i++) {
            char c = (char) buffer.get();
            System.out.println(c);
        }
        System.out.println("position" + buffer.position());
        System.out.println("最大操作索引" + buffer.limit());
        System.out.println("capacity" + buffer.capacity());
    }
}
