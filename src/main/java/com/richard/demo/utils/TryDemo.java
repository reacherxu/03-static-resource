package com.richard.demo.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/9/10 9:42 AM richard.xu Exp $
 */
@Slf4j
public class TryDemo {

    public static String test1() {
        try {
            testReturn(10);
            log.info("try......");
        } finally {
            log.info("finally......");
        }
        log.info("after finally....");
        return "test1";
    }

    private static void testReturn(int i) {
        if (i == 10) {
            return;
        }
        log.info("testReturn");
    }

    public static void main(String[] args) {
        log.info(test1());
    }
}
