package com.ymplans.patterns.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理
 *
 * @author Jos
 */
public class JdkDynamicProxy {
    public static void main(String[] args) {
        UserService userService = new UserService();
        IUserService proxyInstance = (IUserService) Proxy.newProxyInstance(userService.getClass().getClassLoader()
                , userService.getClass().getInterfaces()
                , new UserServiceInvocationHandler(userService));
        proxyInstance.save();
    }
}

class UserServiceInvocationHandler implements InvocationHandler{

    private final IUserService target;

    UserServiceInvocationHandler(IUserService target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用" + method.getName() + "之前");
        Object invoke = method.invoke(target, args);
        System.out.println("调用" + method.getName() + "之后");
        return invoke;
    }
}
