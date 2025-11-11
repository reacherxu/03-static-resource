package com.richard.demo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.richard.demo.services.RemoteService;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CircuitBreakerService {

    @Autowired
    private RemoteService remoteService;

    @Autowired
    @Qualifier("customCircuitBreaker")
    private CircuitBreaker circuitBreaker;

    public Integer circuitBreakerProcess(int i) {
        return Try.ofSupplier(circuitBreaker.decorateSupplier(() -> remoteService.process(i))).recover(ex -> {
            // Handle the exception and provide a fallback value
            log.error("Circuit breaker process failed with exception", ex);
            return -1; // Fallback value
        }).get();
    }
}
