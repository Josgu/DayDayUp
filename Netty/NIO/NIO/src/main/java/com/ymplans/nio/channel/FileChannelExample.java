package com.ymplans.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * FileChannel使用的例子
 *
 * @author Jos
 */

public class FileChannelExample {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("src/test/resources/FileChannelExample.txt", "rw")){
            // 获取文件的Channel
            FileChannel channel = file.getChannel();
            // 输出文件大小
            ByteBuffer writeBuffer = StandardCharsets.UTF_8.encode("GoodGoodStudyDayDayUp GoodGoodStudyDayDayUp GoodGoodStudyDayDayUp ");
            while (writeBuffer.hasRemaining()){
                channel.write(writeBuffer);
            }
            // 强制保存数据和元数据
            channel.force(true);
            System.out.println("文件大小为： " + channel.size());
            // 截取文件
            channel.truncate(44);
            System.out.println("截取后的文件大小为： " + channel.size());
            System.out.println("channel 的 position为：" + channel.position());
            // 设置position
            channel.position(0);
            System.out.println("channel 的 position为：" + channel.position());
            // 创建一个大小22字节的Buffer
            ByteBuffer buffer = ByteBuffer.allocate(22);
            int read = channel.read(buffer); // 将文件内容写入buffer
            while (read != -1){// 是否读到文件尾部

                buffer.flip(); // 切换成读模式

                while (buffer.hasRemaining()){ // 是否读完
                    System.out.print((char) buffer.get());
                }
                System.out.println();

                buffer.clear(); // 清空buffer 并切换成写模式
                read = channel.read(buffer); // 将文件写入buffer
            }
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
