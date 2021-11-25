package com.ymplans.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 分散读例子
 *
 * @author Jos
 */
public class ScatterReadExample {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("src/test/resources/ScatterRead.txt", "rw")){
            FileChannel channel = file.getChannel();
            ByteBuffer aBuffer = ByteBuffer.allocate(13);
            ByteBuffer bBuffer = ByteBuffer.allocate(8);
            channel.read(new ByteBuffer[]{aBuffer, bBuffer});
            printBuffer(aBuffer);
            printBuffer(bBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printBuffer(ByteBuffer aBuffer) {
        aBuffer.flip();
        while (aBuffer.hasRemaining()){
            System.out.print((char) aBuffer.get());
        }
        System.out.println();
    }

}
