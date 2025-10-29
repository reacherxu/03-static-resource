package com.richard.demo.config;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/4/29 16:25 richard.xu Exp $
 */

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class RequestInterceptor implements HandlerInterceptor {

    private static final List<String> paramSet = Arrays.asList("traceId");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.setParam(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex)
            throws Exception {
        MDC.clear();
    }

    private void setParam(HttpServletRequest request) {
        // 设置要放到MDC中的参数
        for (String key : paramSet) {
            String val = request.getHeader(key);
            if (!StringUtils.isEmpty(val)) {
                MDC.put(key, val);
            }
        }
    }

}
