package com.richard.demo.utils;

import org.junit.Test;

import com.richard.demo.utils.pojos.TestClass1;
import com.richard.demo.utils.pojos.TestClass1Sub;
import com.richard.demo.utils.pojos.TestClass2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChainUtil {

    @Test
    public void testBuilder() {
        TestClass1 testClass1 =  TestClass1.builder().name("test1").age(10).married(false).build();
        log.info(testClass1.toString());
        log.info("size is {}", testClass1.getHouses().size());
        testClass1.getHouses().forEach(house -> log.info("house is {}", house));

    }

    /**
     * toBuilder属性默认关闭，如果开启，则所有的父类应该也要开启，效果如下：
     */
    @Test
    public void testSuperBuilder() {
        TestClass1Sub testClass1Sub = TestClass1Sub.builder().age(18).name("sub").married(false).subProperty("sub property").build();
        System.out.println(JacksonUtil.writeStr(testClass1Sub));
    }

    @Test
    public void testChain() {
        TestClass2 testClass2 = new com.richard.demo.utils.pojos.TestClass2();
        testClass2.setName("test2").setAge(12).setMarried(true);
        log.info(testClass2.toString());
    }
}






