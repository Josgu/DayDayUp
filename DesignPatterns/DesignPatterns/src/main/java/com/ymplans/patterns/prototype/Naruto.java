package com.ymplans.patterns.prototype;

/**
 * 鸣人类
 *
 * @author Jos
 */
public class Naruto implements Cloneable{

    // 身高
    public Double height;

    // 体重
    public Double weight;

    // 人柱力
    private String biJuu;

    public Naruto() {}

    public void setBiJuu(String biJuu) {
        this.biJuu = biJuu;
    }

    @Override
    public String toString() {
        return "Naruto{" +
                "height=" + height +
                ", weight=" + weight +
                ", biJuu='" + biJuu + '\'' +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class Main{
    public static void main(String[] args) throws CloneNotSupportedException {
        Naruto naruto = new Naruto();
        naruto.setBiJuu("Kurama");
        naruto.height = 180D;
        naruto.weight = 60D;
        Naruto narutoCopy = (Naruto) naruto.clone();
        System.out.println(narutoCopy);
    }
}

