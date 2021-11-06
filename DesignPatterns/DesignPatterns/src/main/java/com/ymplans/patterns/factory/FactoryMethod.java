package com.ymplans.patterns.factory;

import java.util.*;

import static com.ymplans.patterns.factory.MessageHandler.mockMsgType;

/**
 * 工厂方法实现
 *
 * @author Jos
 */
public class FactoryMethod {
    public static void main(String[] args) throws Exception {
        // mock随机的消息类型，后面对象的解析也假设以new直接创建
        String msgType = mockMsgType();
        String originMsg = "msg";
        IMsgParseFactory iMsgParseFactory = MsgParserFactoryMap.getParserFactory(msgType);
        IMsg msg = iMsgParseFactory.parseMsg(msgType, originMsg);
        // 业务处理逻辑，将数据保存
        System.out.println("保存了 " + msg.getMsg());
    }
}

class MsgParserFactoryMap{
    private static final Map<String, IMsgParseFactory> cachedFactories = new HashMap<>();

    static {
        cachedFactories.put("temp", new TempMsgParseFactory());
        cachedFactories.put("geo", new GeoMsgParseFactory());
        cachedFactories.put("humi", new HumiMsgParseFactory());
    }

    public static IMsgParseFactory getParserFactory(String msgType){
        if (msgType != null && !msgType.isEmpty()){
            return cachedFactories.get(msgType);
        }
        return null;
    }
}

/**
 * 消息对象工厂类
 */
interface IMsgParseFactory {
    IMsg parseMsg(String msgType, String originMsg);
}


/**
 * 温度消息工厂类
 */
class TempMsgParseFactory implements IMsgParseFactory{

    @Override
    public IMsg parseMsg(String msgType, String originMsg) {
        System.out.println("解析原始温度" + originMsg);
        return new TempMsg(10);
    }
}

/**
 * 地理位置消息工厂类
 */
class GeoMsgParseFactory implements IMsgParseFactory{

    @Override
    public IMsg parseMsg(String msgType, String originMsg) {
        System.out.println("解析原始地理位置" + originMsg);
        return new GeoMsg(100.00, 45.00);
    }
}

class HumiMsgParseFactory implements IMsgParseFactory{

    @Override
    public IMsg parseMsg(String msgType, String originMsg) {
        System.out.println("解析原始湿度" + originMsg);
        return new HumiMsg(30);
    }
}