package com.ymplans.patterns.proxy;

/**
 * 第三方客户端
 *
 * @author Jos
 */
public class ThirdClient {

    public void select(){
        System.out.println("查询了一条数据");
    }

    public void update(){
        System.out.println("更新了一条数据");
    }
}
