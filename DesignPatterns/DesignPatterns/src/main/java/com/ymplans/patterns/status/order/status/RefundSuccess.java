package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class RefundSuccess implements OrderStatus {
    private final OrderContext orderContext;

    public RefundSuccess(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "退款成功";
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
