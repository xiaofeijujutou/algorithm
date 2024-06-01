package com.xiaofei.algorithm.netty.test;

import com.xiaofei.algorithm.netty.util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PacketCongestion {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        //                     11            24
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        Queue<ByteBuffer> split = split(source);

        source.put("w are you?\nhaha!\n".getBytes());
        split.addAll(split(source));
        for (ByteBuffer byteBuffer : split) {
            ByteBufferUtil.debugAll(byteBuffer);
        }

    }

    private static Queue<ByteBuffer> split(ByteBuffer source) {
        Queue<ByteBuffer> split = new ArrayDeque<>();

        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n'){
                int len = i - source.position() + 1;
                ByteBuffer splited = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    splited.put(source.get());
                }
                split.add(splited);
            }
        }
        //半包的数据,直接紧凑留住;
        source.compact();
        return split;
    }

}