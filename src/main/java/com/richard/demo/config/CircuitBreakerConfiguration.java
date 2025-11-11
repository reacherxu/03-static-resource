package com.richard.demo.config;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;

@Configuration
@PropertySource("classpath:application.properties")
@Slf4j
public class CircuitBreakerConfiguration {
    @Value("${resilience4j.circuitbreaker.instances.customCircuitBreaker.failure-rate-threshold:50}")
    private Float failureRateThreshold;
    @Value("${resilience4j.circuitbreaker.instances.customCircuitBreaker.sliding-window-size:10}")
    private Integer slidingWindowSize;

    @Value("${resilience4j.circuitbreaker.instances.customCircuitBreaker.minimum-number-of-calls:4}")
    private Integer minimumNumberOfCalls;

    @Value("${resilience4j.circuitbreaker.instances.customCircuitBreaker.wait-duration-in-open-state:PT60S}")
    private Duration waitDurationInOpenState;

    @Bean(name = "customCircuitBreaker")
    public CircuitBreaker customCircuitBreaker() {
        // 踩坑提示：默认情况下 Exception 不会被视为失败，需显式配置 recordException(Exception.class) 才能捕获。
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom().failureRateThreshold(failureRateThreshold)
                .slidingWindowSize(slidingWindowSize).slidingWindowType(COUNT_BASED).minimumNumberOfCalls(minimumNumberOfCalls)
                .waitDurationInOpenState(waitDurationInOpenState).recordExceptions(RuntimeException.class).build();

        // can use user defined config
        // CircuitBreakerConfig circuitBreakerConfig =
        // CircuitBreakerConfig.custom().failureRateThreshold(20)
        // .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED).slidingWindowSize(5).build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("customCircuitBreaker");
        circuitBreaker.getEventPublisher().onStateTransition(evnt -> log.warn("customCircuitBreaker state changes from {} to {}",
                evnt.getStateTransition().getFromState(), evnt.getStateTransition().getToState()));
        return circuitBreaker;
    }

}
