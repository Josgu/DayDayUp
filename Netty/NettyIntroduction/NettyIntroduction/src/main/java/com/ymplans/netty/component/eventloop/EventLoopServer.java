package com.ymplans.netty.component.eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author Jos
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        EventLoopGroup defaultGroup = new DefaultEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                // boos 和 worker
                // 前一个参数是Boss EventLoopGroup用来处理 ServerSocketChannel 中的 accept事件
                // 后一个参数是Worker EventLoopGroup用来处理 SocketChannel上的读写事件
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                // 此handler任务将由 workerGroup中的线程处理，workerGroup线程还需要处理io事件
                                .addLast("handler1",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.debug(byteBuf.toString(Charset.defaultCharset()));
                                // 将worker线程任务传递给下一个handler的线程进行执行
                                ctx.fireChannelRead(msg);
                            }
                        })
                                // 此handler任务将由 defaultGroup中的线程处理，可以处理一些耗时的操作
                                // 不会影响到workerGroup处理io事件
                                .addLast(defaultGroup, "handler2", new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.debug(byteBuf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                });
        serverBootstrap.bind(9904);
    }
}
