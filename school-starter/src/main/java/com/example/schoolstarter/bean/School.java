package com.example.schoolstarter.bean;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
public class School implements ISchool {

    // Resource
    @Autowired() //primary
    private Klass class1;

    @Resource
    private Student student100;

    @Override
    public void ding() {
        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student100);
    }
}
