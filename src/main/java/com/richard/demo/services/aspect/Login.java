package com.richard.demo.services.aspect;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//注解类型，级别
@Documented
@Retention(RetentionPolicy.RUNTIME)//运行时注解
public @interface Login {
    String username() default "";
    String password() default "";
}