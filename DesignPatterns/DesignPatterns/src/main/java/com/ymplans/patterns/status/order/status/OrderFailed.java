package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class OrderFailed implements OrderStatus {
    private final OrderContext orderContext;

    public OrderFailed(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "订单失败";
    }

    @Override
    public void doSuccess() {
        System.out.println("发起退款流程成功, 更新数据库状态");
        this.orderContext.setOrderStatus(new PendingRefund(this.orderContext));
    }

    @Override
    public void doFailed() {
        System.out.println("发起退款失败，再发起退款");
        // 成功
        doSuccess();
        // 多次失败，归档异常订单
    }
}
