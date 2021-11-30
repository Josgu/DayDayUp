# ChannelFuture组件

## ChannelFuture 
Netty中的IO操作都是异步的，所以为了让调用者知道处理的结果设计了ChannelFuture接口，而ChannelFuture有两种状态
- 未完成: 非失败，非成功，非取消
- 完成: 成功，失败，取消

```Java
package com.ymplans.netty.component.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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

        channelFuture.sync();
        Channel channel = channelFuture.channel();
        ChannelFuture hi = channel.writeAndFlush("Hi");
    }
}
```
在上面客户端代码中bootstrap进行了connect和channel发送了一条数据后，都返回了channelFuture。其中connect方法是一个异步非阻塞的方法，也就意味着connect的执行交给了其他线程进行了执行。
## Channel的常见方法

### sync方法
在上面的客户端中使用了sync方法，这个方法是一个阻塞方法，直到上面的connect返回结果后才不阻塞才继续执行后面的代码
```Java
channelFuture.sync();
```

### addListener方法
这个方法是非阻塞的，main线程不会被阻塞，这里的代码将由处理connect连接的nio线程进行回调，执行future的内容。
```Java
channelFuture.addListener((ChannelFutureListener) future -> {
    Channel channel = future.channel();
    channel.writeAndFlush("hi");
});
```
### await方法 
await是一个阻塞方法，其具体的作用和sync一样，但是相对于sync少了一个异常的检查抛出

### channel方法 
channel方法可以获取到channelFuture相关联的channel，此处的Channel是Netty实现的网络抽象类，除了包含NIO中Channel的网络操作，主动关闭/建立连接，获取双方网络地址的功能外，还提供了Netty框架的功能，比如获取绑定的EventLoop，相关的pipeline等。
