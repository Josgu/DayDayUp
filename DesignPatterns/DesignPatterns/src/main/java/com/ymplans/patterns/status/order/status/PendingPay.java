package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class PendingPay implements OrderStatus {

    private final OrderContext orderContext;

    public PendingPay(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "待支付";
    }

    @Override
    public void doSuccess() {
        System.out.println("更新数据库状态为支付成功");
        orderContext.setOrderStatus(new PaySuccess(this.orderContext));
    }

    @Override
    public void doFailed() {
        System.out.println("更新数据库状态为已取消");
        orderContext.setOrderStatus(new OrderCancel(this.orderContext));
    }
}
