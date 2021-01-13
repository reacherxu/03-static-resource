package com.richard.demo.utils.reflection;

import org.springframework.stereotype.Component;

@Component
public class TestPrint {

    private String print(String arg1, String arg2) {
        return String.format("%s, %s",arg1,arg2);
    }
}


