package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class OrderDeleted implements OrderStatus {

    private final OrderContext orderContext;

    public OrderDeleted(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "已删除";
    }

    @Override
    public void doSuccess() {

    }

    @Override
    public void doFailed() {

    }
}
