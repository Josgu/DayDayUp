package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class OrderFinish implements OrderStatus {
    private final OrderContext orderContext;

    public OrderFinish(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "订单完成";
    }

    @Override
    public void doSuccess() {
        System.out.println("规则校验，订单逻辑删除");
        this.orderContext.setOrderStatus(new OrderDeleted(this.orderContext));
    }

    @Override
    public void doFailed() {

    }
}
