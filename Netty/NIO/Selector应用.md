# Selector应用
在[SocketChannel和ServerSocketChannel](SocketChannel&ServerSocketChannel.md)中介绍到了阻塞模式和非阻塞模式下会出现的问题，阻塞模式会让线程不能正常的处理多客户端的时间，非阻塞模式下造成CPU资源的浪费，在NIO中少不了[Selector](Selector.md)来解决这些问题


## 客户端 
```Java
package com.ymplans.selector.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * Client
 *
 * @author Jos
 */
public class Client {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8889));
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
```
客户端创建了一个SocketChannel，并连接了服务端发送了两条消息后正常进行了关闭

## 单线程Selector版服务端 
```Java
package com.ymplans.selector.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashSet;
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
```
上面代码中遵循了下面步骤，来实现Selector版本的服务端
1. 创建Selector
2. 创建ServerSocketChannel，设置为非阻塞模式，注册到Selector上，并监听连接事件
3. 循环调用select获取事件（阻塞方法）
4. 遍历已发生的事件集合
5. 根据SelectionKey类型做对应处理
6. 移除处理的SelectionKey
通过上述代码可以发现Selector将服务端变成了阻塞和非阻塞模式的结合，Selector阻塞的监听事件，当有连接和读事件时，就进行相应的处理

## 多线程Selector版服务端
```Java
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
```
多线程版本中核心就是让多个Thread与多个Selector相关联，上述代码中，主线程关联的Selector进行Accept事件的处理，并将连接的读事件监听处理工作交给新创建的Selector和Worker线程，其中一个较为核心的点Worker线程启动时会阻塞在run方法中的select方法上，等待事件的发生，但是此时channel的注册在主线程中，会发生线程还没有注册上，所以需要让channel成功注册上后才能正常的监听事件的发生。  
这里使用了队列实现两个线程之间的通信，保证子线程Worker的Selector能够正常监听处理事件。