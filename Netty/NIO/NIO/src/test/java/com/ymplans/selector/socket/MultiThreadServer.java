package com.ymplans.selector.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程版本服务端
 *
 * @author Jos
 */
@Slf4j
public class MultiThreadServer {


    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss-thread");
        Selector bossSelector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8890));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        SelectionKey severSelectionKey = serverSocketChannel.register(bossSelector, SelectionKey.OP_ACCEPT);
        log.debug("register key:{}", severSelectionKey);
        Worker[] workers = new Worker[2];
        for(int i = 0; i < workers.length; i++){
            workers[i] = new Worker("worker" + i);
        }
        AtomicInteger count = new AtomicInteger();
        while (true){
            bossSelector.select();
            Iterator<SelectionKey> iterator = bossSelector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if (key.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    log.debug("accept {}", socketChannel);
                    socketChannel.configureBlocking(false);
                    workers[count.getAndIncrement() % workers.length].register(socketChannel);
                }
                iterator.remove();
            }
        }

    }
}
@Slf4j
class Worker implements Runnable{
    private Selector selector;
    private final String name;
    private volatile boolean flag = false;
    private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();


    Worker(String name) {
        this.name = name;
    }
    public void register(SocketChannel socketChannel) throws IOException {
        if (!flag){
            Thread thread = new Thread(this, name);
            selector = Selector.open();
            flag = true;
            thread.start();
        }

        // boss线程给worker线程发送注册消息
        queue.add(()->{
            try {
                socketChannel.register(this.selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        });
        // 唤醒select方法，让worker线程消费注册消息
        selector.wakeup();
    }

    @Override
    public void run() {
        while (true) {
            try {
                selector.select();
                Runnable runnable = queue.poll();
                if (runnable != null){
                    runnable.run();
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()){
                        try {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(48);
                            SocketChannel channel = (SocketChannel) key.channel();
                            log.debug("read {}", channel.getRemoteAddress());
                            int read = channel.read(byteBuffer);
                            if (read == -1){
                                key.cancel();
                                log.debug("disconnect {} ", channel.getRemoteAddress());
                                continue;
                            }
                            byteBuffer.flip();
                            while (byteBuffer.hasRemaining()){
                                System.out.print((char) byteBuffer.get());
                            }
                            System.out.println();
                        } catch (IOException e) {
                            key.cancel();
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
