# SocketChannel&ServerSocketChannel

SocketChannel是连接到Tcp网络套接字的通道，我们可以通过它编写客户端与服务端进行连接，而ServerSocketChannel是一个可以监听Tcp连接的Channel，通过ServerSocketChannel可用创建一个服务端来处理客户端的链接，下面会先介绍两个类的常用方法，并编写一个服务端和一个客户端来理解这两个类
## ServerSocketChannel
### 创建
ServerSocketChannel可以通过调用open()方法来创建
```Java
ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
```
### 关闭
ServerSocketChannel可以通过调用close方法来关闭
```Java
serverSocketChannel.close();
```
### 绑定监听端口
ServerSocketChannel可以绑定监听具体端口的Tcp连接
```
serverSocketChannel.socket().bind(new InetSocketAddress(8888));
```
### 监听连接
可以通过调用accept方法来来监听传入的连接，accept是一个阻塞的方法，当一个连接连上，accept方法会返回一个SocketChannel
```Java
SocketChannel socketChannel = serverSocketChannel.accept();
```
### 阻塞模式切换
ServerSocketChannel是可以通过configureBlock方法来实现阻塞模式和非阻塞模式的切换
```Java
serverSocketChannel.configureBlock(false);
```
### 服务端
```Java
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
            // serverSocketChannel.configureBlocking(false); 阻塞模式下注释
            while (true){
                System.out.println("等待连接中"); // 非阻塞模式下注释
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel!=null && socketChannel.isConnected()){
                    // socketChannel.configureBlocking(false); // 阻塞模式下注释
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
```
在上面代码中，创建了一个ServerSocketChannel，监听了8888端口，为了可以连接多个客户端，将accept方法放入循环中，并将连接上的客户端通道保存了起来，这里服务端没有开启非阻塞模式，所以当程序运行起来，会输出等待连接中并阻塞在accept方法处，并等待客户端连接。
## SocketChannel 
SocketChannel 作为 Channel，其读写数据也是通过Buffer进行处理，但是它可以设置阻塞模式，可以调整读写数据和连接方法connect的阻塞模式
### 创建
SocketChannel除了在ServerSocketChannel处理新连接时会创建外，还可以通过open方法来打开一个SocketChannel
```Java
SocketChannel socketChannel = SocketChannel.open();
```

### 连接远程服务器
可以通过connect方法连接远程服务器，例如连接上文中的服务器
```Java
socketChannel.connect(new InetSocketAddress("localhost", 8888));
```
### 客户端
```Java
package com.ymplans.channel.socket;

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
```
在上面客户端中，连接了服务端 localhost 的 8888端口，并向channel中写入了两条消息。

## 阻塞模式 
在上文的服务端中有一行注释的代码
```Java
serverSocketChannel.configureBlocking(false);
```
如果没有设置非阻塞，上面的代码有一个很大的问题，循环的消息，最终只能收到一条，最终的运行结果是：
```
等待连接中
/127.0.0.1:65499已连接
hi server, I am/127.0.0.1:65499
等待连接中
```
这是因为线程阻塞在accept方法上，在等待下一个客户端的连接，所以对各个channel的数据没有进行有效的处理，另外如果一个客户端连接上后不发送消息，一直让线程阻塞在read方法上导致无法监听新的连接，阻塞模式不能很好的处理多客户端的连接

## 非阻塞模式
服务器修改成非阻塞模式后，accept方法 和 read方法不再阻塞住线程，可以发现等待连接的日志会快速在控制台打印，这是程序在快速的空转，抢占了大量的CPU资源，在没有客户端连接或者发送消息造成了资源的浪费
