package com.ymplans.channel.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 服务端
 *
 * @author Jos
 */
public class Server {
    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.socket().bind(new InetSocketAddress(8888));
            ByteBuffer byteBuffer = ByteBuffer.allocate(40);
            HashSet<SocketChannel> channelSet = new HashSet<>();
            serverSocketChannel.configureBlocking(false); // 阻塞模式下注释
            while (true){
                // System.out.println("等待连接中");  // 非阻塞模式下注释
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel!=null && socketChannel.isConnected()){
                    socketChannel.configureBlocking(false); // 阻塞模式下注释
                    channelSet.add(socketChannel);
                    System.out.println(socketChannel.getRemoteAddress() + "已连接");
                }
                Iterator<SocketChannel> iterator = channelSet.iterator();
                while (iterator.hasNext()){
                    SocketChannel channel = iterator.next();
                    // 阻塞方法
                    int read = channel.read(byteBuffer);
                    // 连接断开
                    if (read == -1){
                        iterator.remove();
                        channel.close();
                        continue;
                    }
                    if (read != 0){
                        byteBuffer.flip();
                        while (byteBuffer.hasRemaining()){
                            System.out.print((char) byteBuffer.get());
                        }
                        System.out.println();
                        byteBuffer.clear();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
