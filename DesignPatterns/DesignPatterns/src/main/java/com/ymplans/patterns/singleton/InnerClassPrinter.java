package com.ymplans.patterns.singleton;

/**
 * 静态内部类单例模式
 *
 * @author Jos
 */
public class InnerClassPrinter {

    private InnerClassPrinter(){}

    private static final class InstanceHolder {
        private static final InnerClassPrinter instance = new InnerClassPrinter();
    }

    public static InnerClassPrinter getInstance() {
        return InstanceHolder.instance;
    }

    public synchronized void print(String file){
        System.out.println("打印机开始打印了");
        System.out.println(file);
    }

}
