/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.richard.demo.enums.OrderType;
import com.richard.demo.services.OrderInfoDao;
import com.richard.demo.services.aspect.Login;
import com.richard.demo.services.impl.OrderInfoServiceImpl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: HelloWorld.java, v 0.1 May 23, 2020 3:31:18 PM richard.xu Exp $
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class HelloWorldController {

    @Autowired
    private Map<String, OrderInfoDao> multiServiceMap;

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

        System.out.println("--------------map--------------");
        for (Map.Entry<String, OrderInfoDao> entry : multiServiceMap.entrySet()) {
            System.out.println("key=" + entry.getKey());
            entry.getValue().queryOrderList();
        }
        return map;
    }

    /**
     * test bean map
     * 
     * @return
     */
    @GetMapping(value = "/springBeanMap")
    @ResponseBody
    public Map<String, OrderInfoDao> springBeanMap() {

        System.out.println("--------------map--------------");
        for (Map.Entry<String, OrderInfoDao> entry : multiServiceMap.entrySet()) {
            System.out.println("key=" + entry.getKey());
            entry.getValue().queryOrderList();
        }
        return multiServiceMap;
    }

    @Autowired
    private OrderInfoServiceImpl orderInfoService;

    /**
     * test bean enum map
     *
     * @return
     */
    @GetMapping(value = "/enumBeanMap/{name}")
    @ResponseBody
    public String enumBeanMap(@PathVariable String name) {
        OrderType type = OrderType.getByName(name);
        return orderInfoService.getOrderService(type).queryOrderList();
    }

    @Autowired
    @Qualifier("sfdDelegateExecutorService")
    private final ExecutorService executorService;

    @PostMapping(value = "/current")
    @ResponseBody
    public List<Integer> current(@RequestParam List<Integer> numbers) {
        List<Integer> result = Observable.from(numbers).flatMap(num -> Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                // if not using DelegatingSecurityContextExecutorService, then can print it
                // ThreadPoolExecutor pool = (ThreadPoolExecutor) executorService;;
                // log.info("thread pool info : active count {},core size {},maxPool size {},blocking queue size
                // {}", pool.getActiveCount(),
                // pool.getCorePoolSize(), pool.getMaximumPoolSize(), pool.getQueue().size());
                if (num % 2 == 0 && num < 10) {
                    log.info("thread is {},sqrt is {}", Thread.currentThread().getId(), num * num);
                } else if (num % 2 != 0) {
                    throw new RuntimeException("do not support odd number");
                } else {
                    log.error("thread is {},sqrt is {}", Thread.currentThread().getId(), num * num);
                }
                return num * num;
            }
        }).subscribeOn(Schedulers.from(executorService)).doOnError(e -> log.warn(e.getMessage()))
                .onErrorResumeNext(response -> Observable.<Integer>empty())).toList().toBlocking().single();
        return result;
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


    /**
     * springboot中动态修改logback日志级别
     * 
     * @return
     */
    @RequestMapping("/logger/printAllLogger")
    public Map printAllLogger() {
        Map result = new HashMap();
        result.put("code", 200);
        result.put("msg", "success");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> loggers = loggerContext.getLoggerList();
        Iterator<ch.qos.logback.classic.Logger> iter = loggers.iterator();
        System.out.println("printAllLogger begin>>>>>>>>");
        while (iter.hasNext()) {
            System.out.println(iter.next().getName());
        }
        System.out.println("printAllLogger end>>>>>>>>");
        return result;
    }

    @RequestMapping("/logger/print")
    public Map loggerPrint() {
        Map result = new HashMap();
        result.put("code", 200);
        result.put("msg", "success");

        log.debug("loggerPrint debug>>>>");
        log.info("loggerPrint info>>>>");
        log.warn("loggerPrint warn>>>>");
        log.error("loggerPrint error>>>>");

        return result;
    }

    @RequestMapping("/logger/level")
    public Map loggerLevelChange(String level) {
        Map result = new HashMap();
        result.put("code", 200);
        result.put("msg", "success");

        // String loggerFactoryClassStr = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();
        // System.out.println("loggerFactoryClassStr>>>>" + loggerFactoryClassStr);

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger("ROOT");
        switch (level) {
            case "debug":
                logger.setLevel(Level.DEBUG);
                break;
            case "info":

                logger.setLevel(Level.INFO);
                break;
            case "warn":
                logger.setLevel(Level.WARN);
                break;
            case "error":
                logger.setLevel(Level.ERROR);
                break;
            default:
                break;
        }
        return result;
    }


    /**
     * test aspect
     * 
     * @return
     */
    @GetMapping(value = "/aspect1")
    @ResponseBody
    @Login(username = "aspect without error", password = "12345")
    public Map<String, Object> aspect1() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "ok");

        System.out.println("--------------this is aspect without errors--------------");
        return map;
    }

    /**
     * test aspect with errors
     *
     * @return
     */
    @GetMapping(value = "/aspect2")
    @ResponseBody
    @Login(username = "aspect without error", password = "errors")
    public Map<String, Object> aspect2() {
        System.out.println("--------------this is aspect with errors--------------");
        throw new RuntimeException("this is aspect with errors");
    }

}
