package com.ymplans.nio.buffer;

import java.nio.ByteBuffer;

/**
 * buffer方法示例程序
 *
 * @author Jos
 */
public class BufferMethodExample {

    public static void main(String[] args) {
        // 生成一个10个字节大小空间ByteBuffer对象
        ByteBuffer buffer = ByteBuffer.allocate(10);
        showBuffer(buffer, "buffer 初始化后：");
        // 写入一个数据
        buffer.put((byte) 65);
        showBuffer(buffer, "buffer 写入一个数据后：");
        // 写入byte数组
        buffer.put(new byte[]{97,98,99,100,101});
        showBuffer(buffer, "buffer 写入一个数组后：");
        // 读模式切换
        buffer.flip();
        showBuffer(buffer, "buffer 切换读模式后：");
        // 读取一个数据
        System.out.println("获取了数据为： " + buffer.get());
        showBuffer(buffer, "buffer 读取一个数据后：");
        // 读取指定数据, 不影响position
        System.out.println("获取了数据为： " + buffer.get(2));
        showBuffer(buffer, "buffer 读取指定数据后：");
        buffer.mark();
        buffer.get();
        showBuffer(buffer, "buffer 调用mark并读取一个数据后：");
        buffer.reset();
        showBuffer(buffer, "buffer 调用reset后：");
        buffer.rewind();
        showBuffer(buffer, "buffer 调用rewind后：");
        buffer.get();
        buffer.compact();
        showBuffer(buffer, "buffer 读取一个数据，并调用compact后：");
    }

    private static void showBuffer(ByteBuffer buffer, String msg) {
        System.out.println(msg);
        System.out.printf("position:  %d,\tlimit:  %d,\tcapacity:  %d%n", buffer.position(), buffer.limit(), buffer.capacity());
    }
}
