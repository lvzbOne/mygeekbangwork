package com.example.schoolstarter.config;

import com.example.schoolstarter.bean.Klass;
import com.example.schoolstarter.bean.School;
import com.example.schoolstarter.bean.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/2
 */
@Configuration
public class AutoConfig {

    @Bean
    @ConditionalOnMissingBean(Student.class)
    public Student student() {
        return Student.create();
    }

    @Bean
    @ConditionalOnMissingBean(Klass.class)
    public Klass klass() {
        Klass klass = new Klass();
        List<Student> students = new ArrayList<>();
        students.add(Student.create());
        klass.setStudents(students);
        return klass;
    }

    @Bean
    @ConditionalOnMissingBean(School.class)
    public School school() {
        return new School();
    }
}
