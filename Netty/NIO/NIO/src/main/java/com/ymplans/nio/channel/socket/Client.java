package com.ymplans.nio.channel.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * 客户端
 *
 * @author Jos
 */
public class Client {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8888));
            ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode("hi server, I am" + socketChannel.getLocalAddress());
            socketChannel.write(byteBuffer);
            Thread.sleep(5000);
            ByteBuffer byteBuffer2 = StandardCharsets.UTF_8.encode(socketChannel.getLocalAddress() + "time: " + LocalDate.now());
            socketChannel.write(byteBuffer2);
            socketChannel.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
