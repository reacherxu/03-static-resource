package com.richard.demo.utils.util;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/4/29 16:15 richard.xu Exp $
 */

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

/**
 * @Description 封装MDC用于向线程池传递
 */
public class MDCUtil {

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (CollectionUtils.isEmpty(context)) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static void setMDCContextMap(final Map<String, String> context) {
        if (CollectionUtils.isEmpty(context)) {
            MDC.clear();
        } else {
            MDC.setContextMap(context);
        }
    }

}
