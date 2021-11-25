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
