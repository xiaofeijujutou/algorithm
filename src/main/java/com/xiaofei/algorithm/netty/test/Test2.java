package com.xiaofei.algorithm.netty.test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Test2 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});
        buffer.flip();
        for (int i = 0; i < 10; i++) {
            System.out.println(buffer.get(new byte[4]).toString());
            buffer.rewind();
        }

    }
}
