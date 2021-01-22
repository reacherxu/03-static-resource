package com.richard.demo.services.impl;

import org.springframework.stereotype.Component;

import com.richard.demo.enums.OrderType;
import com.richard.demo.services.OrderInfoDao;

@Component
public class OrderInfoDaoAImpl implements OrderInfoDao {
    @Override
    public String queryOrderList() {
        System.out.println("Dao A queryOrderList");
        return "Dao A queryOrderList";
    }

    @Override
    public OrderType getOrderType() {
        return OrderType.OrderA;
    }

}
