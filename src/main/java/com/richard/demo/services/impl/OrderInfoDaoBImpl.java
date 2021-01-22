package com.richard.demo.services.impl;

import org.springframework.stereotype.Component;

import com.richard.demo.enums.OrderType;
import com.richard.demo.services.OrderInfoDao;

/** * 实现类B */
@Component
public class OrderInfoDaoBImpl implements OrderInfoDao {
    @Override
    public String queryOrderList() {
        System.out.println("Dao B queryOrderList");
        return "Dao B queryOrderList";
    }

    @Override
    public OrderType getOrderType() {
        return OrderType.OrderB;
    }

}

