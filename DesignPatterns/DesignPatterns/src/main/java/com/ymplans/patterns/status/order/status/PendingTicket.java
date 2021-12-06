package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class PendingTicket implements OrderStatus {
    private final OrderContext orderContext;

    public PendingTicket(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "出票中";
    }

    @Override
    public void doSuccess() {
        System.out.println("保存票据系统下发的票，更新数据库状态");
        this.orderContext.setOrderStatus(new OrderFinish(this.orderContext));
    }

    @Override
    public void doFailed() {
        System.out.println("更新数据库订单失败");
        this.orderContext.setOrderStatus(new OrderFailed(this.orderContext));
    }
}
