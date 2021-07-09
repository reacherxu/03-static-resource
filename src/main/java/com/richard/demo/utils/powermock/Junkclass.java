package com.richard.demo.utils.powermock;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/7/9 4:34 PM richard.xu Exp $
 */
public class Junkclass {
    public String tests(){
       String value =  System.getenv("values");
        String xx[] = value.split(",");
        for (int i = 0; i < xx.length; i++) {
            return xx[i];
        }
        return null;
    }
}
