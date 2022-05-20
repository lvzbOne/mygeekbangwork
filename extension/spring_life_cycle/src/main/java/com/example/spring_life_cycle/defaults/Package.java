package com.example.spring_life_cycle.defaults;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/10
 */
public class Package extends DefaultClass implements DefaultInterface {
    public static void main(String[] args) {
        DefaultClass defaultClass = new DefaultClass();
        defaultClass.setDe("aaaa");
        System.out.println(defaultClass.getDe());

        Package aPackage = new Package();
        aPackage.sayHello();
    }
}
