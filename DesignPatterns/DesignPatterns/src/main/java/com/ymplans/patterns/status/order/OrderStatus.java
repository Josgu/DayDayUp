package com.ymplans.patterns.status.order;

/**
 * @author Jos
 */
public interface OrderStatus {
    String getName();
    void doSuccess();
    void doFailed();
}
