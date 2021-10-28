package com.richard.demo.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChainUtil {

    @Test
    public void testBuilder() {
        TestClass1 testClass1 =  TestClass1.builder().name("test1").age(10).married(false).build();
        log.info(testClass1.toString());
        log.info("size is {}", testClass1.getHouses().size());
        testClass1.getHouses().forEach(house -> {
            log.info("house is {}", house);
        });
    }

    @Test
    public void testChain() {
        TestClass2 testClass2 = new TestClass2();
        testClass2.setName("test2").setAge(12).setMarried(true);
        log.info(testClass2.toString());
    }
}

@Data
@Builder
class TestClass1 {
    private String name;
    private Integer age;
    private Boolean married;

    // 用于不使用默认null的builder的构造函数
    @Builder.Default
    private List<String> houses = new ArrayList<>();

}

@Data
@Accessors(chain = true)
class TestClass2 {
    private String name;
    private Integer age;
    private Boolean married;
    private List<String> houses = new ArrayList<>();
}
