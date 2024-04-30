package com.xiaofei.algorithm.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description: Created by IntelliJ IDEA.
 * @Author : 小肥居居头
 * @create 2024/4/28 12:00
 */


public class ChannelCopy {
    public static void main(String[] args) throws Exception{
        FileInputStream fis = new FileInputStream("src/main/java/com/xiaofei/algorithm/nio/channel/file/狗.png");
        FileOutputStream fos = new FileOutputStream("src/main/java/com/xiaofei/algorithm/nio/channel/file/狗1.png");
        //先把数据导入内存;
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        fosChannel.transferFrom(fisChannel, fisChannel.position(), fisChannel.size());
        fisChannel.close();
        fosChannel.close();
    }
}
