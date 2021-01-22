package com.richard.demo.config;

public class MyBean {
    private String port;

    public void init() {
        System.out.println("MyBean开始初始化...");
    }

    public void destroy() {
        System.out.println("MyBean销毁...");
    }

    public String get() {
        return "端口号： " + getPort();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}