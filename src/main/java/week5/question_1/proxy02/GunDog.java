package week5.question_1.proxy02;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/29
 */
public class GunDog implements Dog {
    @Override
    public void info() {
        System.out.println("我是一只猎狗");
    }

    @Override
    public void run() {
        System.out.println("我跑的飞快！");
    }
}
