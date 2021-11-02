package com.ymplans.patterns.singleton;

/**
 * 饿汉式打印机类
 *
 * @author Jos
 */
public class HungryPrinter {

    // 唯一对象
    private static final HungryPrinter instance = new HungryPrinter();

    // 私有构造方法
    private HungryPrinter(){}

    // 提供可访问的唯一实例
    public static HungryPrinter getInstance() {
        return instance;
    }

    // 添加对象级别的锁
    public synchronized void print(String file){
        System.out.println("打印机开始打印了");
        System.out.println(file);
    }
}

class ServiceA {
    public void printA(){
        HungryPrinter printer = HungryPrinter.getInstance();
        printer.print("printA");
    }
}

class ServiceB {
    public void printB(){
        HungryPrinter printer = HungryPrinter.getInstance();
        printer.print("printB");
    }
}