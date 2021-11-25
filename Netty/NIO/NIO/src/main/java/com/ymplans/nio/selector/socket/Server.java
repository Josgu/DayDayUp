package com.ymplans.nio.selector.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Selector版Server
 *
 * @author Jos
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 非阻塞
        serverSocketChannel.configureBlocking(false);
        // 监听连接事件
        SelectionKey severSelectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.debug("register key:{}", severSelectionKey);
        serverSocketChannel.bind(new InetSocketAddress(8889));

        while (true){
            // 阻塞方法 没有事件发生阻塞，有事件则不阻塞
            selector.select();
            // 遍历已发生的事件集合
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                log.debug("handle key:{}", selectionKey);
                // 处理连接事件
                if (selectionKey.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(48);
                    SocketChannel socketChannel = channel.accept();
                    // 非阻塞
                    socketChannel.configureBlocking(false);
                    // 监听读事件，添加Buffer附件
                    socketChannel.register(selector, SelectionKey.OP_READ, byteBuffer);
                }
                // 处理读事件
                else if (selectionKey.isReadable()){
                    try {
                        SocketChannel channel = (SocketChannel)selectionKey.channel();
                        // 获取附件
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        int read = channel.read(byteBuffer);
                        // 客户端正常断开
                        if (read == -1){
                            // 移除key
                            selectionKey.cancel();
                            continue;
                        }
                        byteBuffer.flip();
                        while (byteBuffer.hasRemaining()){
                            System.out.print((char) byteBuffer.get());
                        }
                        System.out.println();
                        byteBuffer.clear();
                    } catch (IOException e) {
                        // 异常移除key
                        selectionKey.cancel();
                        e.printStackTrace();
                    }
                }
                // 将selectionKey 从事件集合中移除
                iterator.remove();
            }
        }
    }
}
