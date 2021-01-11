package com.richard.demo.utils;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ChainUtil {

    @Test
    public void testBuilder() {
        TestClass1 testClass1 =  TestClass1.builder().name("test1").age(10).married(false).build();
        log.info(testClass1.toString());
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
}

@Data
@Accessors(chain = true)
class TestClass2 {
    private String name;
    private Integer age;
    private Boolean married;
}
