package com.xiaofei.algorithm.netty.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Test1 {
    public static void main(String[] args) {
        String filePath = "D:\\desktop_d\\workspace\\algorithm\\src\\main\\webapp\\first.html";
        try (FileChannel channel = new FileInputStream(filePath).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            int read = 0;
            while ((read = channel.read(buffer)) != -1){
                buffer.flip();//切换成读模式
                while (buffer.hasRemaining()){
                    byte b = buffer.get();//获取一个字节
                    System.out.println((char)b);
                }
                buffer.clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
      }
}
