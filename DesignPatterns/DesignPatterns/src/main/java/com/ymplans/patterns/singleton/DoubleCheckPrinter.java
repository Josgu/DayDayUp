package com.ymplans.patterns.singleton;

/**
 * 双重检测单例模式
 *
 * @author Jos
 */
public class DoubleCheckPrinter {

    private static volatile DoubleCheckPrinter instance;

    private DoubleCheckPrinter(){}

    public static DoubleCheckPrinter getInstance() {
        if (null == instance){
            // 类锁
            synchronized (DoubleCheckPrinter.class){
                if (null == instance){
                    instance = new DoubleCheckPrinter();
                }
            }
        }
        return instance;
    }

    public synchronized void print(String file){
        System.out.println("打印机开始打印了");
        System.out.println(file);
    }
}
