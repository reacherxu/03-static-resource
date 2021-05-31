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
    private static final int CORE_POOL_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 60;

    /**
     * 
     * @return
     */
    @Bean
    public ExecutorService executorService() {
        int corePoolSize = CORE_POOL_SIZE;
        try {
            corePoolSize = Integer.parseInt(SystemUtils.getEnvironmentVariable("DEFAULT_CORE_POOL_SIZE", Integer.toString(CORE_POOL_SIZE)));
        } catch (NumberFormatException ex) {
            log.warn("'coreThreadPoolSize' can't be parsed into integer, set to default [{}]", CORE_POOL_SIZE);
        }
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    /**
     * 在异步线程中使用SecurityContextHolder ， 需要将父线程的securityConext传播到异步线程中，
     * 实现方式是使用spring提供的一个代理线程池DelegatingSecurityContextExecutorService 来执行异步任务
     * Scheduler.io() 不会传递securityConext， 有可能有线程串的风险
     * 
     * @param executorService
     * @return
     */
    @Qualifier("sfdDelegateExecutorService")
    @Bean
    public DelegatingSecurityContextExecutorService getDelelatingExecutorService(ExecutorService executorService) {
        return new DelegatingSecurityContextExecutorService(executorService);
    }


}
