package com.xiaofei.algorithm.nio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/4/28 11:20
 */


public class ChannelWrite {
    public static void main(String[] args) throws Exception{
        FileOutputStream fos = new FileOutputStream("src/main/java/com/xiaofei/algorithm/nio/channel/file/test.txt");
        //OutputStream是不能获取通道的,文件输出才能;
        FileChannel fileChannel = fos.getChannel();
        //创建缓冲区,调用flip准备操作
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("臭居居,臭居不了一点abc".getBytes());
        buffer.flip();
        //直接写入
        fileChannel.write(buffer);
        fileChannel.close();

    }
}
