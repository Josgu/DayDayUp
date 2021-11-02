package com.ymplans.patterns.singleton;

/**
 * 懒汉式单例模式
 *
 * @author Jos
 */
public class LazyPrinter {

    private static LazyPrinter instance;

    private LazyPrinter(){}

    public static synchronized LazyPrinter getInstance() {
        if (null == instance){
            instance = new LazyPrinter();
        }
        return instance;
    }

    public synchronized void print(String file){
        System.out.println("打印机开始打印了");
        System.out.println(file);
    }
}
