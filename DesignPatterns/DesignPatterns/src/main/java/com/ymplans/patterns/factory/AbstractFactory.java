package com.ymplans.patterns.factory;

/**
 * 消息对象工厂类
 */
interface IMsgAbstractParseFactory {
    IAMsg parseAMsg(String msgType, String originMsg);
    IBMsg parseBMsg(String msgType, String originMsg);
}

interface IAMsg {

}

interface IBMsg {

}

class TempMsgParserFactory implements IMsgAbstractParseFactory{
    @Override
    public IAMsg parseAMsg(String msgType, String originMsg) {
        // 具体实现
        return null;
    }

    @Override
    public IBMsg parseBMsg(String msgType, String originMsg) {
        // 具体实现
        return null;
    }
}