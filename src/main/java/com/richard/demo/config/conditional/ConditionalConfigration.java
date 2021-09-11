package com.richard.demo.config.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * test conditional annotation
 */
@Configuration
public class ConditionalConfigration {

    /// test conditonal
    @Bean
    public DependedBean dependedBean() {
        return new DependedBean();
    }

    /**
     * 只有当DependedBean 存在时，才会创建bean: `LoadIfBeanExist`
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(name = "dependedBean")
    public LoadIfBeanExist loadIfBeanExist() {
        return new LoadIfBeanExist("dependedBean");
    }

    /**
     * 只有当没有notExistsBean时，才会创建bean: `LoadIfBeanNotExists`
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "notExistsBean")
    public LoadIfBeanNotExists loadIfBeanNotExists() {
        return new LoadIfBeanNotExists("notExistsBean");
    }

    /**
     * 当引用了 {@link DependedClz} 类之后，才会创建bean： `LoadIfClzExists`
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(DependedClz.class)
    public LoadIfClzExists loadIfClzExists() {
        return new LoadIfClzExists("dependedClz");
    }

    /**
     * 当系统中没有 com.git.hui.boot.conditionbean.example.depends.clz.DependedClz类时，才会创建这个bean
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingClass("com.git.hui.boot.conditionbean.example.depends.clz.DependedClz")
    public LoadIfClzNotExists loadIfClzNotExists() {
        return new LoadIfClzNotExists("com.git.hui.boot.conditionbean.example.depends.clz.DependedClz");
    }
}
