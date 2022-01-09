package com.richard.demo.services.impl;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.richard.demo.enums.OrderType;
import com.richard.demo.services.OrderInfoDao;

@Component
public class OrderInfoServiceImpl implements ApplicationContextAware {

    // usage 1
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

    // usage 2
    private final Map<OrderType, OrderInfoDao> orderInfoMap = new EnumMap<>(OrderType.class);

    @Autowired
    private OrderInfoDaoAImpl orderInfoDaoA;
    @Autowired
    private OrderInfoDaoBImpl orderInfoDaoB;

    @PostConstruct
    public void initialDeployerMap() {
        orderInfoMap.put(OrderType.OrderA, orderInfoDaoA);
        orderInfoMap.put(OrderType.OrderB, orderInfoDaoB);
    }

    public OrderInfoDao getOrder(OrderType typeEnum) {
        return orderInfoMap.get(typeEnum);
    }

}

