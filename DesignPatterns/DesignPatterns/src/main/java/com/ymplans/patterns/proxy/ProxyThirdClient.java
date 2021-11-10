package com.ymplans.patterns.proxy;

/**
 * 代理客户端
 *
 * @author Jos
 */
public class ProxyThirdClient extends ThirdClient{
    @Override
    public void select(){
        // 功能增强
        System.out.println("有人查数据了");
        super.select();
    }
    @Override
    public void update(){
        // 功能增强
        System.out.println("有人更新数据了");
        super.update();
    }
}

class Main {
    public static void main(String[] args) {
        ThirdClient proxyThirdClient = new ProxyThirdClient();
        proxyThirdClient.select();
        proxyThirdClient.update();
    }
}