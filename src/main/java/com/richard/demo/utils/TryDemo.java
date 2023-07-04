package com.richard.demo.utils;

import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;

import com.richard.demo.enums.OrderType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/9/10 9:42 AM richard.xu Exp $
 */
@Slf4j
public class TryDemo {

    public static String test1(int i) {
        try {
            OrderType type1 = OrderType.OrderA;
            testReturn(i, type1);
            log.info("try......,type1 is {}", type1);
        } finally {
            log.info("finally......");
        }
        log.info("after finally....");
        return "test1";
    }

    private static void testReturn(int i, OrderType type1) {
        if (i == 10) {
            type1 = OrderType.OrderB;
            return;
        }
        log.info("testReturn");
    }

    public static void main(String[] args) {
        log.info("spring version {}, springboot version {}", SpringVersion.getVersion(), SpringBootVersion.getVersion());
        log.info(test1(10));

        String slashs = "/vv///bb";
        String temps[] = slashs.split("/");
        log.info("len is {}", temps.length);
    }
}
