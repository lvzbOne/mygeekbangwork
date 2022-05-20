package com.example.spring_life_cycle.defaults;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/10
 */
public interface DefaultInterface {

    /**
     * 默认的普通方法
     */
    default void sayHello() {
        System.out.println("Hello!");
    }
}
