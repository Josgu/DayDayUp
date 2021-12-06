package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class PaySuccess implements OrderStatus {

    private final OrderContext orderContext;

    public PaySuccess(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "支付成功";
    }

    @Override
    public void doSuccess() {
        System.out.println("更新数据库为出票中");
        this.orderContext.setOrderStatus(new PendingTicket(this.orderContext));
    }

    @Override
    public void doFailed() {
    }
}
