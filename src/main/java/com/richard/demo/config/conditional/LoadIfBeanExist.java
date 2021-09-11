package com.richard.demo.config.conditional;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/9/8 10:08 AM richard.xu Exp $
 */
public class LoadIfBeanExist {
    private String name;

    public LoadIfBeanExist(String name) {
        this.name = name;
    }

    public String getName() {
        return "load if bean exists: " + name;
    }
}
