package com.richard.demo.config.conditional;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/9/8 10:20 AM richard.xu Exp $
 */
public class LoadIfClzExists {
    private String name;

    public LoadIfClzExists(String name) {
        this.name = name;
    }

    public String getName() {
        return "load if exists clz: " + name;
    }
}
