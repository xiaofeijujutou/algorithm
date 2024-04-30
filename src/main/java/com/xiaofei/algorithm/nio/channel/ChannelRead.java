package com.xiaofei.algorithm.nio.channel;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/4/28 11:41
 */


public class ChannelRead {
    public static void main(String[] args) throws Exception{
        FileInputStream fis = new FileInputStream("src/main/java/com/xiaofei/algorithm/nio/channel/file/test.txt");
        FileChannel channel = fis.getChannel();
        //创建的时候buffer里面肯定是空数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //channel调用read方法,就可以吧channel里面的数据传输到buffer里面;
        channel.read(buffer);
        //然后从buffer里面读取数据,最好是指定长度;
        buffer.flip();
        String fileData = new String(buffer.array(), 0, buffer.remaining());
        System.out.println(fileData);
    }
}
