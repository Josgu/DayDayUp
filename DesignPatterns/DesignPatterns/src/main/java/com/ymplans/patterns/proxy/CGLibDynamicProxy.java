package com.ymplans.patterns.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib动态代理
 *
 * @author Jos
 */
public class CGLibDynamicProxy {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new ThirdClientMethodInterceptor());
        enhancer.setClassLoader(ThirdClient.class.getClassLoader());
        enhancer.setSuperclass(ThirdClient.class);
        ThirdClient thirdClient = (ThirdClient)enhancer.create();
        thirdClient.select();

    }
}

class ThirdClientMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("调用" + method.getName() + "之前");
        Object ob = methodProxy.invokeSuper(o, objects);
        System.out.println("调用" + method.getName() + "之后");
        return ob;
    }
}
