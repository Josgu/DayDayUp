package com.ymplans.patterns.proxy;

/**
 * 静态代理
 *
 * @author Jos
 */
public class StaticProxy {

    public static void main(String[] args) {
        UserServiceProxy userServiceProxy = new UserServiceProxy(new UserService());
        userServiceProxy.save();
    }

}


interface IUserService{
    void save();
}

class UserService implements IUserService{
    @Override
    public void save() {
        System.out.println("保存了用户");
    }
}

class UserServiceProxy implements IUserService{

    private final UserService userService;

    UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void save() {
        System.out.println("保存用户之前");
        userService.save();
        System.out.println("保存用户之后");
    }
}
