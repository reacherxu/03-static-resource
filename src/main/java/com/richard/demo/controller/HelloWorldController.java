/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: HelloWorld.java, v 0.1 May 23, 2020 3:31:18 PM richard.xu Exp $
 */
@RestController
@Slf4j
public class HelloWorldController {

    /**
     * http://localhost:8080/hello
     * 
     * @return
     */
    @GetMapping(value = "/hello")
    @ResponseBody
    public Map<String, Object> helloWorld() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "ok");
        return map;
    }

    /**
     * test i18n
     * 注意： postman 中测试，messages_zh_CN 对应header中 Accept-Language 中value 为zh-CN
     *
     * 或者用param 方式传送， uncomment WebConfig.java, use localhost:8080/messages?lang=en-US
     */
    @Autowired
    MessageSource messageSource;

    @GetMapping("/messages")
    public String messages() {
        String message = messageSource.getMessage("welcome", null, LocaleContextHolder.getLocale());
        log.info(message);
        return message;
    }

}
