package com.ymplans.patterns.responsibility;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于数组实现
 *
 * @author Jos
 */
public class ChainOfResponsibilityExample2 {
    public static void main(String[] args) {
        HandlerChainTwo handlerChainTwo = new HandlerChainTwo();
        handlerChainTwo.addHandler(new HandlerC());
        handlerChainTwo.addHandler(new HandlerD());
        handlerChainTwo.handle();
    }
}

interface IHandler {
    boolean handle();
}

class HandlerC implements IHandler {

    @Override
    public boolean handle() {
        return false;
    }
}

class HandlerD implements IHandler {

    @Override
    public boolean handle() {
        return false;
    }
}

class HandlerChainTwo {
    private final List<IHandler> chain = new ArrayList<>();

    public void addHandler(IHandler handler){
        chain.add(handler);
    }

    public void handle(){
        for (IHandler handler : chain) {
            boolean handle = handler.handle();
            if (handle){
                break;
            }
        }
    }
}