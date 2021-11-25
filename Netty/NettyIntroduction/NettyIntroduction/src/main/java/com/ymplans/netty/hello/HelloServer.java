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
