# Hello Netty  
本文将使用Netty开发一个简易的服务器端和客户端，实现客户端向服务端发送Hello Netty，服务端接受并打印

## 服务器端
首先需要引入Netty的依赖
```xml
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.70.Final</version>
</dependency>
```
编写服务端代码
```Java
package com.ymplans.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author Jos
 */
public class HelloServer {
    public static void main(String[] args) {
        // 1. ServerBootstrap 负责组装netty组件，来启动服务器
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                // 2. 创建EventLoop，EventLoop用来处理 accept read write时间 // 14. EventLoop监听到read事件 接收ByteBuf
                .group(new NioEventLoopGroup())
                // 3. 选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                // 4. childHandler 配置了读写操作时会做的事情
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    // 11. 客户端连接建立，初始化与客户端进行读写的通道
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 添加StringDecoder字符串解码器 // 15. 将接收的ByteBuf转换为字符串
                        nioSocketChannel.pipeline().addLast(new StringDecoder());
                        // 添加自定义入站处理器处理读事件  // 16. 执行自定义入站处理器read方法
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 17. 打印Hello Netty!
                                System.out.println(msg);
                            }
                        });
                    }
                });
        // 5. 绑定监听端口
        serverBootstrap.bind(9903);
    }
}
```
## 客户端 
```Java
package com.ymplans.netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author Jos
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        // 6. 客户端启动类配置
        Bootstrap bootstrap = new Bootstrap()
                // 7. 添加EventLoop组处理事件
                .group(new NioEventLoopGroup())
                // 8. 选择客户端SocketChannel的实现
                .channel(NioSocketChannel.class)
                // 9. 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    // 11. 与服务端连接时被调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 添加StringEncoder字符串编码器
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });
        // 10. 连接服务器
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 9903));
        // 12. sync 阻塞直到连接建立，获取到channel，发送数据Hello Netty！
        // 13. 通过pipeline的StringEncoder将Hello Netty！转换为ByteBuf
        channelFuture.sync().channel().writeAndFlush("Hello Netty!");
    }
}
```
## 流程分析
如代码中的注释所示  
服务端由ServerBootStrap创建，并配置了以下参数
- EventLoopGroup 来监听accept、read、write等事件
- 配置了服务端ServerSocketChannel的实现方式
- 配置了子处理器来处理事件
- 客户端连接上后实现初始化，在pipeline中定义了解码器StringDecoder 和 自定义读时间处理器 
- 绑定监听端口9903

客户端由BootStrap创建，并配置了以下参数   
- EventLoopGroup 来处理读写事件
- 配置了服务端SocketChannel的实现方式
- 添加了处理器处理事件
- 连接建立后，在pipeline中定义了编码器StringEncoder
- 配置连接服务器 localhost:8080

在客户端完成配置后客户端会被sync()方法阻塞，直到连接建立后，可以获取到与服务器连接的Channel，并向服务器发送了Hello Netty!，而消息则会通过以下步骤进行处理
1. 客户端pipeline中的StringEncoder将消息转换成ByteBuf类型
2. 服务器EventLoop监听到read事件，并接收到ByteBuf
3. 服务端pipeline中的StringDecoder将ByteBuf解码
4. 服务端pipeline中的自定义处理器通过read事件处理方法将消息打印