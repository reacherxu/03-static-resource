package com.richard.demo.enums;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public enum OrderType {

    OrderA("OrderA", 0),
    OrderB("OrderB", 1),
    ;

    private String name;
    private int key;

    private static Map<String, OrderType> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        for (OrderType type : OrderType.values()) {
            map.put(type.name, type);
        }
    }

    OrderType(String name, int key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public static OrderType getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return map.get(name);
    }

    public static OrderType getByKey(int key) {
        for (OrderType type : OrderType.values()) {
            if (type.key == key) {
                return type;
            }
        }
        return null;
    }
}
