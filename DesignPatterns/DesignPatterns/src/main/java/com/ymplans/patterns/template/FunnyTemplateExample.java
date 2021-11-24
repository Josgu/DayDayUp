package com.ymplans.patterns.template;

/**
 * @author Jos
 */
public class FunnyTemplateExample {
    public static void main(String[] args) {
        UniversalFormula deliciousApple = new DeliciousApple();
        deliciousApple.templateFormula();
        UniversalFormula deliciousBanana = new DeliciousBanana();
        deliciousBanana.templateFormula();
    }
}

abstract class UniversalFormula {
    public final void templateFormula(){
        System.out.println("剩下的" + getFood() + "不要扔，裹上鸡蛋液，粘上面包糠，下锅炸至金黄酥脆控油捞出，老人小孩都爱吃，隔壁小孩都馋哭了。");
    }
    protected abstract String getFood();
}

class DeliciousApple extends  UniversalFormula {

    @Override
    protected String getFood() {
        return "苹果";
    }
}

class DeliciousBanana extends UniversalFormula {

    @Override
    protected String getFood() {
        return "香蕉";
    }
}

