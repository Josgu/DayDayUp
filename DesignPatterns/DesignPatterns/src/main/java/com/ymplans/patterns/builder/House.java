package com.ymplans.patterns.builder;

/**
 * 大房子类
 *
 * @author Jos
 */
public class House {

    // 面积
    private final Double area;

    // 楼层
    private final Integer floor;

    // 材料
    private final String material;

    // 中央空调
    private final Boolean centerAir;

    // 卧室
    private final Double bedroomArea;

    // 床
    private final Double bedArea;

    @Override
    public String toString() {
        return "House{" +
                "area=" + area +
                ", floor=" + floor +
                ", material='" + material + '\'' +
                ", centerAir=" + centerAir +
                ", bedroomArea=" + bedroomArea +
                ", bedArea=" + bedArea +
                '}';
    }

    // 私有构造方法
    private House(Builder builder) {
        this.area = builder.area;
        this.bedArea = builder.bedArea;
        this.floor = builder.floor;
        this.material = builder.material;
        this.bedroomArea = builder.bedroomArea;
        this.centerAir = builder.centerAir;
    }


    public static class Builder{
        // 面积
        private Double area;

        // 楼层
        private Integer floor;

        // 材料
        private String material;

        // 中央空调
        private Boolean centerAir;

        // 卧室
        private Double bedroomArea;

        // 床
        private Double bedArea;

        public House build(){
            // 校验逻辑放到这里来做，包括必填项校验、依赖关系校验、约束条件校验等
            if (area == null || floor == null || material == null || bedroomArea == null){
                throw new IllegalArgumentException("缺少必要参数");
            }
            if (bedArea >= bedroomArea || bedroomArea >= area){
                throw new IllegalArgumentException("房子建大点啊");
            }
            if (centerAir == null){
                centerAir = false;
            }
            return new House(this);
        }

        public Builder setArea(Double area) {
            if (area < 40){
                throw new IllegalArgumentException("房子太小了，对得起写代码的你吗");
            }
            this.area = area;
            return this;
        }

        public Builder setFloor(Integer floor) {
            if (floor < 1){
                throw new IllegalArgumentException();
            }
            this.floor = floor;
            return this;
        }

        public Builder setMaterial(String material) {
            this.material = material;
            return this;
        }

        public Builder setCenterAir(Boolean centerAir) {
            if (centerAir == null){
                throw new IllegalArgumentException();
            }
            this.centerAir = centerAir;
            return this;
        }

        public Builder setBedroomArea(Double bedroomArea) {
            if (bedroomArea < 10){
                throw new IllegalArgumentException("卧室也忒小了");
            }
            this.bedroomArea = bedroomArea;
            return this;
        }

        public Builder setBedArea(Double bedArea) {
            if (bedArea < 2){
                throw new IllegalArgumentException("床也忒小了");
            }
            this.bedArea = bedArea;
            return this;
        }
    }
}

class BuildMyHouse{
    public static void main(String[] args) {
        House goldHouse = new House.Builder()
                .setArea(200.0)
                .setBedroomArea(30.0)
                .setBedArea(5.0)
                .setFloor(3)
                .setMaterial("gold")
                .setCenterAir(true).build();
        System.out.println(goldHouse.toString());
    }
}