package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class PendingRefund implements OrderStatus {

    private final OrderContext orderContext;

    public PendingRefund(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "退款中";
    }

    @Override
    public void doSuccess() {
        System.out.println("退款成功，更新数据库状态");
        this.orderContext.setOrderStatus(new RefundSuccess(this.orderContext));
    }

    @Override
    public void doFailed() {
        System.out.println("退款失败, 更新数据库状态");
        this.orderContext.setOrderStatus(new RefundFailed(this.orderContext));
    }
}
