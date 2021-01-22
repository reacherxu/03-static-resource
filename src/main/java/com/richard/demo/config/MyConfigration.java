package com.richard.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfigration {
    @Bean(initMethod="init", destroyMethod="destroy")
    public MyBean myBean() {
        MyBean myBean = new MyBean();
        myBean.setPort("8080");
        return myBean;
    }
}
