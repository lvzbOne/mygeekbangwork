package com.example.dynamic_data_source.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 测试注解
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface M1 {
}
