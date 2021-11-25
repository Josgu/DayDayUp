package com.ymplans.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 集中写例子
 *
 * @author Jos
 */
public class GatheringWriteExample {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("src/test/resources/GatheringWrite.txt", "rw")){
            FileChannel channel = file.getChannel();
            ByteBuffer aBuffer = ByteBuffer.allocate(20);
            ByteBuffer bBuffer = ByteBuffer.allocate(10);
            aBuffer.put("GoodGoodStudy".getBytes(StandardCharsets.UTF_8));
            bBuffer.put("DayDayUp".getBytes(StandardCharsets.UTF_8));
            // 切换到读模式
            aBuffer.flip();
            bBuffer.flip();
            channel.write(new ByteBuffer[]{aBuffer, bBuffer});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
