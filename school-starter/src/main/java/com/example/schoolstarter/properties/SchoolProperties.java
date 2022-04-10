package com.example.schoolstarter.properties;

import com.example.schoolstarter.bean.Klass;
import com.example.schoolstarter.bean.Student;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/10
 */
@Data
@ConfigurationProperties(prefix = "school")
@PropertySource("classpath:application.properties")
public class SchoolProperties {

    private String prefix;
//    private List<Student> students;
//    private List<Klass> classes;
}
