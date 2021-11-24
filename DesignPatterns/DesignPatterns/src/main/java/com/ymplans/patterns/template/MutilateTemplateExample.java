package com.ymplans.patterns.template;

/**
 * @author Jos
 */
public class MutilateTemplateExample {
    public static void main(String[] args) {

    }
}

// 回调接口
interface ICallback {
    void methodToCallback();
}

// 模板超类
abstract class AbstractClass {
    public final void templateMethod1(ICallback callback) {
        callback.methodToCallback();
    }
    public final void templateMethod2(ICallback callback) {
        callback.methodToCallback();
    }
}

// 回调实现1
class ConcreteCallback1 implements ICallback{
    @Override
    public void methodToCallback() {
        method1();
        method2();
    }
    public void method1(){}
    public void method2(){}
}

// 回调实现2
class ConcreteCallback2 implements ICallback{
    @Override
    public void methodToCallback() {
        method3();
        method4();
    }
    public void method3(){}
    public void method4(){}
}

// 老版本模板超类
abstract class AbstractTemplateClass {
    public final void templateMethod1() {
        //...
        method1();
        //...
        method2();
        //...
    }

    public final void templateMethod2() {
        //...
        method3();
        //...
        method4();
        //...
    }

    protected abstract void method1();
    protected abstract void method2();
    protected abstract void method3();
    protected abstract void method4();
}