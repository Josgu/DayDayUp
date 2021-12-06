package com.ymplans.patterns.status;

import com.ymplans.patterns.status.order.OrderContext;

/**
 * @author Jos
 */
public class StatusExample {

    public static void main(String[] args) {
        System.out.println("-------成功订单流程---------");
        successOrder();
        System.out.println("-------支付失败订单流程---------");
        payFailedOrder();
        System.out.println("-------出票失败成功退款流程---------");
        ticketFailedPayRefundOrder();
        System.out.println("-------出票失败退款失败流程---------");
        ticketFailedRefundFailedOrder();
    }

    private static void ticketFailedRefundFailedOrder() {
        System.out.println("订单初始化");
        OrderContext orderContext = new OrderContext();
        System.out.println("接收到订单支付成功结果");
        orderContext.successAction();
        System.out.println("调用票务系统出票");
        orderContext.successAction();
        System.out.println("票务系统出票失败");
        orderContext.failedAction();
        System.out.println("发起退款流程");
        orderContext.failedAction();
        System.out.println("用户删除了订单");
    }

    private static void ticketFailedPayRefundOrder() {
        System.out.println("订单初始化");
        OrderContext orderContext = new OrderContext();
        System.out.println("接收到订单支付成功结果");
        orderContext.successAction();
        System.out.println("调用票务系统出票");
        orderContext.successAction();
        System.out.println("票务系统出票失败");
        orderContext.failedAction();
        System.out.println("发起退款流程");
        orderContext.successAction();
        System.out.println("接受到支付系统退款成功");
        orderContext.successAction();
        System.out.println("用户删除了订单");
        orderContext.successAction();
    }

    private static void payFailedOrder() {
        System.out.println("订单初始化");
        OrderContext orderContext = new OrderContext();
        System.out.println("接收到订单支付失败结果，用户超时未支付");
        orderContext.failedAction();
        System.out.println("用户删除了订单");
        orderContext.successAction();
    }

    public static void successOrder(){
        System.out.println("订单初始化");
        OrderContext orderContext = new OrderContext();
        System.out.println("接收到订单支付成功结果");
        orderContext.successAction();
        System.out.println("调用票务系统出票");
        orderContext.successAction();
        System.out.println("票务系统出票成功");
        orderContext.successAction();
        System.out.println("用户删除了订单");
        orderContext.successAction();
    }

}



