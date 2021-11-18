package com.ymplans.patterns.adapter;

/**
 * @author Jos
 */
public class AdapterExample {
    public static void main(String[] args) {
        // 电脑只支持typeC接口
        Computer computer = new Computer();
        // 显示器的Hdmi线
        HdmiInterFace hdmiInterFace = new HdmiInterFace();
        // 转接器
        TypeCInterFaceAdapter typeCInterFaceAdapter = new TypeCInterFaceAdapter();
        // 转接器接入hdmi
        typeCInterFaceAdapter.insertTypeCInterFace(hdmiInterFace);
        // 转接器插入电脑
        computer.insert(typeCInterFaceAdapter);
    }
}

class Computer{
    public void insert(TypeCInterFace typeCInterFace){
        typeCInterFace.getType();
    }
}

class HdmiInterFace {
    public void getType(){
        System.out.println("Hdmi接口");
    }
}

class TypeCInterFace {
    public void getType(){
        System.out.println("TypeC接口");
    }
}

class TypeCInterFaceAdapter extends TypeCInterFace{
    private HdmiInterFace hdmiInterFace;

    @Override
    public void getType(){
        System.out.println("适配接口：");
        super.getType();
        System.out.println("接入接口：");
        this.hdmiInterFace.getType();
    }

    public void insertTypeCInterFace(HdmiInterFace hdmiInterFace) {
        this.hdmiInterFace = hdmiInterFace;
    }
}