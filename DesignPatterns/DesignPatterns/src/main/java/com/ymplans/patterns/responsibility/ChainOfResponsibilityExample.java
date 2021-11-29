package com.ymplans.patterns.responsibility;

/**
 * 基于链表实现
 * @author Jos
 */
public class ChainOfResponsibilityExample {

    public static void main(String[] args) {
        HandlerChain handlerChain = new HandlerChain();
        handlerChain.addHandler(new HandlerA());
        handlerChain.addHandler(new HandlerB());
        handlerChain.handle();
    }
}

abstract class Handler {
    protected Handler successor = null;

    public void setSuccessor(Handler successor){
        this.successor = successor;
    }

    public final void handle(){
        boolean handled = doHandle();
        if (null!=successor && !handled) {
            successor.handle();
        }
    }

    protected abstract boolean doHandle();
}

class HandlerA extends Handler {

    @Override
    protected boolean doHandle() {
        // 省略handle逻辑
        return false;
    }
}

class HandlerB extends Handler {

    @Override
    protected boolean doHandle() {
        // 省略handle逻辑
        return false;
    }
}

class HandlerChain {
    private Handler head = null;
    private Handler tail = null;

    public void addHandler(Handler handler){
        handler.setSuccessor(null);
        if (head == null){
            head = handler;
            tail = handler;
            return;
        }
        tail.setSuccessor(handler);
        tail = handler;
    }

    public void handle(){
        if (head != null){
            head.handle();
        }
    }
}