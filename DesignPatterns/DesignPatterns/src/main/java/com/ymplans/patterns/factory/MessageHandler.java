package com.ymplans.patterns.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 消息处理器
 *
 * @author Jos
 */
public class MessageHandler {

    public static void main(String[] args) throws Exception {
        // mock随机的消息类型，后面对象的解析也假设以new直接创建
        String msgType = mockMsgType();
        String originMsg = "msg";
        // 通过工厂创建对象
        IMsg msg = MsgParseFactory.parseMsg(msgType, originMsg);

        // 业务处理逻辑，将数据保存
        System.out.println("保存了 " + msg.getMsg());
    }

    public static String mockMsgType() {
        List<String> types = Arrays.asList("temp", "geo", "humi");
        Random random = new Random();
        return types.get(random.nextInt(2));
    }
}

/**
 * 消息对象工厂类
 */
class MsgParseFactory {
    public static IMsg parseMsg(String msgType, String originMsg) throws Exception {
        IMsg msg;
        if ("temp".equals(msgType)){
            System.out.println("解析原始温度" + originMsg);
            msg = new TempMsg(10);
        }else if ("geo".equals(msgType)){
            System.out.println("解析原始地理位置" + originMsg);
            msg = new GeoMsg(100.00, 45.00);
        }else if ("humi".equals(msgType)){
            System.out.println("解析原始湿度" + originMsg);
            msg = new HumiMsg(30);
        }else {
            throw new Exception("未识别的消息类型");
        }
        return msg;
    }
}

/**
 * 标准消息体
 */
interface IMsg{
    String getMsg();
}

/**
 * 温度消息体
 */
class TempMsg implements IMsg{

    // 温度
    private final double temp;

    TempMsg(double temp) {
        this.temp = temp;
    }

    public String getMsg(){
        return "温度数据" + temp;
    }
}

/**
 * 地理信息
 */
class GeoMsg implements IMsg{
    private final double x;
    private final double y;

    GeoMsg(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String getMsg() {
        return "经度：" + x + " 纬度："+ y;
    }
}
/**
 * 湿度信息
 */
class HumiMsg implements IMsg{

    private final double humi;

    HumiMsg(double humi) {
        this.humi = humi;
    }

    @Override
    public String getMsg() {
        return "湿度：" + humi;
    }
}

