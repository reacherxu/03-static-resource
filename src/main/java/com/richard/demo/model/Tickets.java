package com.richard.demo.model;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/8/11 16:14 richard.xu Exp $
 */

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.MDC;

import com.richard.demo.utils.util.MDCUtil;

public class Tickets<String> implements Callable<String> {
    Map<java.lang.String, java.lang.String> copyOfContextMap;

    public Tickets(Map<java.lang.String, java.lang.String> copyOfContextMap) {
        this.copyOfContextMap = copyOfContextMap;
    }

    // 重写call方法
    @Override
    public String call() throws Exception {
        MDCUtil.setMDCContextMap(copyOfContextMap);
        System.out.println(Thread.currentThread().getName() + "-->我是通过实现Callable接口通过FutureTask包装器来实现的线程," + MDC.get("traceId"));
        return (String) "tst01";
    }
}