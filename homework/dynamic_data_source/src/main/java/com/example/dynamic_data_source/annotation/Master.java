package com.example.dynamic_data_source.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识主服务
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/15
 */
//@Deprecated
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Master {
}
