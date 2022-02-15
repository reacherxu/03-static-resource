package com.richard.demo.services.impl;

import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.richard.demo.services.RetryService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/2/14 2:10 PM richard.xu Exp $
 */
@Service
@Slf4j
public class RetryServiceImpl implements RetryService {
    @Override
    @Retryable(value=TimeoutException.class,maxAttempts = 5, backoff = @Backoff(delay = 2000L, multiplier = 2))
    public void service(String command) throws Exception {
        if(StringUtils.equals("success",command)) {
            log.info("calling retry service success...");

        } else if (StringUtils.equals("other-exception",command)) {
            log.warn("calling retry service other exception");
            throw new TimeoutException("service other exception");
        } else {
            log.warn("calling retry service timeout exception");
            throw new TimeoutException("service timeout exception");
        }
    }

    @Override
    @Recover
    public void recover(TimeoutException e) {
        log.info("recovering service");
    }
}
