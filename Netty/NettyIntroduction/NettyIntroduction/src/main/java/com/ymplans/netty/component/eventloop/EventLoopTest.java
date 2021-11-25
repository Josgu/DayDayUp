package com.ymplans.netty.component.eventloop;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Jos
 */
@Slf4j
public class EventLoopTest {
    public static void main(String[] args) {
        // 1. 创建EventLoopGroup
        // io 事件，普通任务，定时任务
        // 构造参数如果为空，创建的线程数为 DEFAULT_EVENT_LOOP_THREADS
        // = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);
        // 普通任务，定时任务
        //DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();

        // 2. 获取下一个EventLoop 一直循环
        System.out.println(nioEventLoopGroup.next());
        System.out.println(nioEventLoopGroup.next());
        System.out.println(nioEventLoopGroup.next());

        // 3. 执行普通任务
        nioEventLoopGroup.next().submit(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("ok");
        });

        // 4. 执行定时任务
        nioEventLoopGroup.next().scheduleAtFixedRate(() ->{
            log.debug("ok2");
        }, 0, 10, TimeUnit.SECONDS);
        log.debug("main");
    }
}
