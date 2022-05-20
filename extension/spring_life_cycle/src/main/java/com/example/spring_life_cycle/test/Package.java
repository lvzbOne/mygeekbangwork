package com.example.spring_life_cycle.test;

import com.example.spring_life_cycle.defaults.DefaultClass;
import com.example.spring_life_cycle.defaults.DefaultInterface;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/10
 */
public class Package extends DefaultClass implements DefaultInterface {
    public static void main(String[] args) {
        DefaultClass defaultClass = new DefaultClass();
        //System.out.println(defaultClass.get);

        Package aPackage = new Package();
        aPackage.sayHello();
    }
}
