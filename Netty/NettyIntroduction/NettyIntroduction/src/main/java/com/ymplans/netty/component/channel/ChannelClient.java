package com.ymplans.netty.component.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author Jos
 */
public class ChannelClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 9904));
        // 同步阻塞 main线程阻塞, 等到nio线程连接成功后执行后续代码
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        channel.writeAndFlush("Hi");
        // 异步执行结果
//        channelFuture.addListener((ChannelFutureListener) future -> {
//            Channel channel = future.channel();
//            channel.writeAndFlush("hi");
//        });
    }
}
