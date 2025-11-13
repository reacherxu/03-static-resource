package com.richard.demo.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RateLimitConfig {

    @Bean("customRateLimiter")
    public RateLimiter customRateLimiter() {
        // allow 2 calls every 10 seconds
        RateLimiterConfig config = RateLimiterConfig.custom().limitForPeriod(2).limitRefreshPeriod(Duration.ofSeconds(10))
                .timeoutDuration(Duration.ofSeconds(10)).build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = registry.rateLimiter("customRateLimiter");
        return rateLimiter;
    }
}
