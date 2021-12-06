package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class OrderCancel implements OrderStatus {

    private final OrderContext orderContext;

    public OrderCancel(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "已取消";
    }

    @Override
    public void doSuccess() {
        System.out.println("校验订单，逻辑删除订单");
        this.orderContext.setOrderStatus(new OrderDeleted(this.orderContext));
    }

    @Override
    public void doFailed() {

    }
}
