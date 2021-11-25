package com.ymplans.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Channel传输例子
 *
 * @author Jos
 */
public class ChannelTransferExample {

    public static void main(String[] args) {
        try (RandomAccessFile fromFile = new RandomAccessFile("src/test/resources/from.txt", "r");
             RandomAccessFile toFile = new RandomAccessFile("src/test/resources/to.txt", "rw")){
            FileChannel fromFileChannel = fromFile.getChannel();
            FileChannel toFileChannel = toFile.getChannel();
            System.out.printf("from文件大小：%d, to文件大小： %d\n", fromFileChannel.size(), toFileChannel.size());
            fromFileChannel.transferTo(0, fromFileChannel.size(),toFileChannel);
            System.out.printf("from文件大小：%d, to文件大小： %d\n", fromFileChannel.size(), toFileChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
