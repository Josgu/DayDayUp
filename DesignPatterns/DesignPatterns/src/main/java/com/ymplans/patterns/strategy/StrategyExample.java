package com.ymplans.patterns.strategy;

import java.util.*;

/**
 * @author Jos
 */
public class StrategyExample {

    // 策略的使用
    public static void main(String[] args) {
        String msgType = mockMsgType();
        String mockMsg = "mock" + msgType;
        MsgStrategy msgStrategy = MsgStrategyFactory.getMsgStrategy(msgType);
        msgStrategy.handlerMsg(mockMsg);
    }

    public static String mockMsgType() {
        List<String> types = Arrays.asList("temp", "geo", "humi");
        Random random = new Random();
        return types.get(random.nextInt(2));
    }

}


// 定义策略，处理消息接口
interface MsgStrategy {
    void handlerMsg(String msg);
}

//定义温度信息处理策略

class TempMsgStrategy implements MsgStrategy{

    @Override
    public void handlerMsg(String msg) {
        // 省略处理温度信息算法
    }
}


class GeoMsgStrategy implements MsgStrategy {

    @Override
    public void handlerMsg(String msg) {
        // 省略处理地理信息算法
    }
}


class HumiMsgStrategy implements MsgStrategy{

    @Override
    public void handlerMsg(String msg) {
        // 省略处理湿度信息算法
    }
}

// 策略创建
class MsgStrategyFactory {
    private static final Map<String, MsgStrategy> strategies = new HashMap<>();

    static {
        strategies.put("geo", new GeoMsgStrategy());
        strategies.put("temp", new TempMsgStrategy());
        strategies.put("humi", new HumiMsgStrategy());
    }

    public static MsgStrategy getMsgStrategy(String type){
        return strategies.get(type);
    }
}