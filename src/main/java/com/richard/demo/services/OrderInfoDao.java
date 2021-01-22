package com.richard.demo.services;

import com.richard.demo.enums.OrderType;

public interface OrderInfoDao {
    public String queryOrderList();

    public OrderType getOrderType();
}
