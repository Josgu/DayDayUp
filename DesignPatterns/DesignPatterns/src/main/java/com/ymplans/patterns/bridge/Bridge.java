package com.ymplans.patterns.bridge;

/**
 * 桥接模式
 *
 * @author Jos
 */
public class Bridge {
    public static void main(String[] args) {
        Device tv = new Tv();
        RemoteControl remoteControl = new RemoteControl(tv);

        Device radio = new Radio();
        AdvancedRemoteControl advancedRemoteControl = new AdvancedRemoteControl(radio);
    }
}


interface Device {

    // 设置音量
    void setVolume(Integer percent);

    // 调台
    void setChannel(String channel);

    // 获取音量
    Integer getVolume();

    // 获取频道
    String getChannel();

}

class Tv implements Device{

    private Integer volume;

    private String channel;

    @Override
    public void setVolume(Integer percent) {
        this.volume = percent;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public Integer getVolume() {
        return this.volume;
    }

    @Override
    public String getChannel() {
        return this.channel;
    }
}

class Radio implements Device{

    private Integer volume;

    private String channel;

    @Override
    public void setVolume(Integer percent) {
        this.volume = percent;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public Integer getVolume() {
        return this.volume;
    }

    @Override
    public String getChannel() {
        return this.channel;
    }
}

class RemoteControl {
    protected final Device device;

    RemoteControl(Device device) {
        this.device = device;
    }

    public void volumeUp(){
        device.setVolume(device.getVolume() + 2);
    }

    public void volumeDown(){
        device.setVolume(device.getVolume() - 2);
    }

    public void setChannel(String channel){
        device.setChannel(channel);
    }
}

class AdvancedRemoteControl extends RemoteControl{

    AdvancedRemoteControl(Device device) {
        super(device);
    }

    public void showDetail(){
        System.out.println(super.device.getChannel() + super.device.getVolume());
    }

    public void voiceSetChannel(String voice){
        // voice 转换 channel
        String channel = voice;
        super.device.setChannel(channel);
    }
}