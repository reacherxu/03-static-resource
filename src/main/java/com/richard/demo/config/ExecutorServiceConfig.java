package com.richard.demo.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ExecutorServiceConfig {
    private static final int DEFAULT_THREADS_NUMBER = 10;
    private static final int KEEP_ALIVE_TIME = 30;
    private static final int WORK_QUEUE_SIZE = 40;

    @Bean
//    @Primary
    public ExecutorService executorService() {
        int poolSize = DEFAULT_THREADS_NUMBER;
        try {
            poolSize = Integer.parseInt(
                    SystemUtils.getEnvironmentVariable("DEFAULT_FIXED_THREADPOOL_SIZE", Integer.toString(DEFAULT_THREADS_NUMBER)));
        } catch (NumberFormatException ex) {
            log.warn("'defaultThreadPoolSize' can't be parsed into integer, set to default [{}]", DEFAULT_THREADS_NUMBER);
        }
        return new ThreadPoolExecutor(poolSize, poolSize + 10, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(WORK_QUEUE_SIZE));
    }

    /*
    在异步线程中使用SecurityContextHolder ， 需要将父线程的securityConext传播到异步线程中，
    实现方式是使用spring提供的一个代理线程池DelegatingSecurityContextExecutorService 来执行异步任务
     */
    @Qualifier("sfdDelegateExecutorService")
    @Bean
    public DelegatingSecurityContextExecutorService getDelelatingExecutorService(ExecutorService executorService) {
        return new DelegatingSecurityContextExecutorService(executorService);
    }
}
