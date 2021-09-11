package com.richard.demo.config.conditional;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/9/8 10:14 AM richard.xu Exp $
 */
public class LoadIfBeanNotExists {
    public String name;

    public LoadIfBeanNotExists(String name) {
        this.name = name;
    }

    public String getName() {
        return "load if bean not exists: " + name;
    }
}
