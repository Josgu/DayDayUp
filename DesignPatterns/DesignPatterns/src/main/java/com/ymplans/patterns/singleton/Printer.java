package com.ymplans.patterns.singleton;

/**
 * 枚举单例模式
 *
 * @author Jos
 */
public enum Printer {
    INSTANCE;
    public synchronized void print(String file){
        System.out.println("打印机开始打印了");
        System.out.println(file);
    }
}
