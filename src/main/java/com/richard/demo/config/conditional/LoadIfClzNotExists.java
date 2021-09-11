package com.richard.demo.config.conditional;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/9/8 10:21 AM richard.xu Exp $
 */
public class LoadIfClzNotExists {
    private String name;

    public LoadIfClzNotExists(String name) {
        this.name = name;
    }

    public String getName() {
        return "load if not exists clz: " + name;
    }
}
