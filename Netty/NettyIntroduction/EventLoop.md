# EventLoop组件

## EventLoop
EventLoop是一个接口，本质上是一个单线程的定时执行器(继承了Java的ScheduledExecutorService) 可以执行一些普通任务或者定时任务，同时EventLoop继承了netty实现的OrderedEventExcutor来保证有序的处理事件。  
EventLoop提供了parent()方法来查找属于哪个EventLoopGroup
```Java
package io.netty.channel;

import io.netty.util.concurrent.OrderedEventExecutor;

public interface EventLoop extends OrderedEventExecutor, EventLoopGroup {
    @Override
    EventLoopGroup parent();
}
```

## EventLoopGroup
EventLoopGroup是一组EventLoop，提供了register()方法来将channel和一个EventLoop绑定，后续channel上的io事件将由相绑定的EventLoop来实现（一个EventLoop可绑定多个Channel），来保证Io事件处理的线程安全。另外还重载了父类EventExecutorGroup的next()方法来获取集合中的下一个EventLoop
```Java
package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

public interface EventLoopGroup extends EventExecutorGroup {

    @Override
    EventLoop next();

    ChannelFuture register(Channel channel);

    ChannelFuture register(ChannelPromise promise);

    @Deprecated
    ChannelFuture register(Channel channel, ChannelPromise promise);
}
```

## EventLoop的使用
### 创建EventLoopGroup
EventLoopGroup是一个接口，他有一些具体的实现，最常用的就是NioEventLoopGroup，可以执行io 事件，普通事件，定时任务，EventLoopGroup还有一些其他实现DefaultEventLoopGroup、KQueueEventLoopGroup。
```Java
// io 事件，普通任务，定时任务
// 构造参数如果为空，创建的线程数为 DEFAULT_EVENT_LOOP_THREADS
// = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);
// 普通任务，定时任务
EventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();
```

### 获取EventLoop
使用next方法即可获取EventLoop，next方法是循环的获取EventLoop，也就是简单的轮询
```Java
System.out.println(nioEventLoopGroup.next());
System.out.println(nioEventLoopGroup.next());
System.out.println(nioEventLoopGroup.next());
```
上面面代码打印出来结果是
```
io.netty.channel.nio.NioEventLoop@124c278f
io.netty.channel.nio.NioEventLoop@15b204a1
io.netty.channel.nio.NioEventLoop@124c278f
```

### 执行普通任务
使用EventLoop的submit接口，可以传入一个任务执行
```Java
nioEventLoopGroup.next().submit(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("ok");
        });
```

### 执行定时任务
使用EventLoop的scheduleAtFixedRate方法，提供参数Runnable，延迟时间，间隔时间，以及时间单位来实现定时任务的实现
```Java
nioEventLoopGroup.next().scheduleAtFixedRate(() ->{
    log.debug("ok2");
}, 0, 10, TimeUnit.SECONDS);
```

### 分工细化
首先看一段服务端的新实现
```Java
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
```
我创建了三个EventGroup: 两个NioEventLoop、一个DefaultEventLoop，上面介绍到NioEventLoop多支持了io事件的处理。  
首先在服务器的配置中group填入了bossGroup和workerGroup，让他们分别做accept事件处理 和 读写事件处理，让线程的工作更加的细化  
其次是在pipeline中加入了两个自定义处理器，假设两个处理器中前者处理耗时较短，后者处理的任务耗时较长。所以为了避免耗时较长的操作影响后续的io事件。我们将handler2与defaultGroup做了绑定，也就意味着这里的处理工作将由defaultGroup中的线程进行处理。而handler1将由workerGroup中的线程进行处理。  
其中在handler1中有一个关键代码`ctx.fireChannelRead(msg);`，此处的代码是进行线程任务的切换。其源码实现如下：
```Java
    @Override
    public ChannelHandlerContext fireChannelRead(final Object msg) {
        invokeChannelRead(findContextInbound(MASK_CHANNEL_READ), msg);
        return this;
    }

    static void invokeChannelRead(final AbstractChannelHandlerContext next, Object msg) {
        final Object m = next.pipeline.touch(ObjectUtil.checkNotNull(msg, "msg"), next);
        // 获取下一个handler的EventLoop
        EventExecutor executor = next.executor();
        // 如果是当前EventLoop
        if (executor.inEventLoop()) {
            next.invokeChannelRead(m);
        } else {
            // 如果不是则新创建线程任务
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    next.invokeChannelRead(m);
                }
            });
        }
    }
```