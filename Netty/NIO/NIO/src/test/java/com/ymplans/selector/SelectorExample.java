package com.ymplans.selector;

import java.io.IOException;
import java.nio.channels.*;

/**
 * 选择器例子
 *
 * @author Jos
 */
public class SelectorExample {
    public static void main(String[] args) {
        try (Selector selector = Selector.open()) {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            // 监听的集合
            System.out.println(selectionKey.interestOps());
            // 准备好的集合
            System.out.println(selectionKey.readyOps());
            // 获取channel
            System.out.println(selectionKey.channel());
            // 获取选择器
            System.out.println(selectionKey.selector());
            // 非阻塞获取准备好的事件
            System.out.println(selector.selectNow());
            // 阻塞获取准备好的事件
            selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
