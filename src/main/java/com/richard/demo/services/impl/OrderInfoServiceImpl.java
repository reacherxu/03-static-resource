package com.richard.demo.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.richard.demo.enums.OrderType;
import com.richard.demo.services.OrderInfoDao;

@Component
public class OrderInfoServiceImpl implements ApplicationContextAware {

    private static Map<OrderType, OrderInfoDao> testServiceMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,OrderInfoDao> map = applicationContext.getBeansOfType(OrderInfoDao.class);
        testServiceMap = new HashMap<>();
        map.forEach((key,value) -> testServiceMap.put(value.getOrderType(),value));
    }

    public OrderInfoDao getOrderService(OrderType typeEnum) {
        return testServiceMap.get(typeEnum);
    }
}

