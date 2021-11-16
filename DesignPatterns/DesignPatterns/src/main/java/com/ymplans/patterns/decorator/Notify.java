package com.ymplans.patterns.decorator;

/**
 * 通知接口
 *
 * @author Jos
 */
public interface Notify {
    public void send(String message);
}

class BaseEmailNotify implements Notify{

    @Override
    public void send(String message) {
        System.out.println("邮件发送：" + message);
    }
}

class SmsDecorator implements Notify{

    private final Notify notify;

    public SmsDecorator(Notify notify) {
        this.notify = notify;
    }

    public void send(String message) {
        this.notify.send(message);
        System.out.println("短信发送：" + message);
    }
}

class WechatDecorator implements Notify{
    private final Notify notify;

    public WechatDecorator(Notify notify){
        this.notify = notify;
    }

    @Override
    public void send(String message) {
        this.notify.send(message);
        System.out.println("微信发送：" + message);
    }
}

class Main{
    public static void main(String[] args) {
        Notify baseEmailNotify = new BaseEmailNotify();
        baseEmailNotify.send("Hi!");

        WechatDecorator wechatDecorator = new WechatDecorator(baseEmailNotify);
        wechatDecorator.send("Java!");

        Notify smsDecorator = new SmsDecorator(baseEmailNotify);
        smsDecorator.send("Hello!");

        Notify aWechatDecorator= new WechatDecorator(smsDecorator);
        aWechatDecorator.send("World!");
    }
}
