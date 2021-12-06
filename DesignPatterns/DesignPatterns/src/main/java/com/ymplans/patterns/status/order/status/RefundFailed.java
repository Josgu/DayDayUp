package com.ymplans.patterns.status.order.status;

import com.ymplans.patterns.status.order.OrderContext;
import com.ymplans.patterns.status.order.OrderStatus;

/**
 * @author Jos
 */
public class RefundFailed implements OrderStatus {
    private final OrderContext orderContext;

    public RefundFailed(OrderContext orderContext) {
        this.orderContext = orderContext;
    }

    @Override
    public String getName() {
        return "退款失败";
    }

    @Override
    public void doSuccess() {
        System.out.println("退款成功，更新数据库状态");
        this.orderContext.setOrderStatus(new RefundSuccess(this.orderContext));
    }

    @Override
    public void doFailed() {
        System.out.println("退款失败, 再发起退款");
        // 成功
        doSuccess();
        // 多次失败 ，异常订单归档
    }
}
