package com.richard.demo;

import static org.mockito.Mockito.*;

import java.time.Duration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.richard.demo.services.RemoteService;
import com.richard.demo.services.impl.CircuitBreakerService;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CircuitBreakerServiceTest {
    @Mock
    private RemoteService remoteService;
    private CircuitBreaker circuitBreaker;

    @InjectMocks
    private CircuitBreakerService circuitBreakerService;

    @Before
    public void setUp() {
        // mock CircuitBreaker
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(4)
                .recordExceptions(RuntimeException.class)
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker customCircuitBreaker = registry.circuitBreaker("customCircuitBreaker");
        ReflectionTestUtils.setField(circuitBreakerService, "circuitBreaker", customCircuitBreaker);
    }

    @Test
    public void testCircuitBreakerProcessSuccess() {
        // 让 remoteService.process 抛出异常，模拟失败
        when(remoteService.process(anyInt())).thenThrow(new RuntimeException());
        for (int i = 0; i < 10; i++) {
            circuitBreakerService.circuitBreakerProcess(i);
        }
        verify(remoteService, times(4)).process(anyInt());
        // 断言断路器状态
        CircuitBreaker customCircuitBreaker = (CircuitBreaker) ReflectionTestUtils.getField(circuitBreakerService, "circuitBreaker");
        assert customCircuitBreaker != null;
        // 断路器应该处于 OPEN 状态
        Assert.assertEquals(CircuitBreaker.State.OPEN, customCircuitBreaker.getState());
    }

}
