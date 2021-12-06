package com.ymplans.patterns.status.order;

import com.ymplans.patterns.status.order.status.PendingPay;

/**
 * @author Jos
 */
public class OrderContext {

    private OrderStatus orderStatus;

    public OrderContext(){
        this.orderStatus = new PendingPay(this);
    }

    public void successAction(){
        this.orderStatus.doSuccess();
    }

    public void failedAction(){
        this.orderStatus.doFailed();
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus.getName();
    }
}
