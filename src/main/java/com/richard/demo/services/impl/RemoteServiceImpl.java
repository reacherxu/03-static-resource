package com.richard.demo.services.impl;

import org.springframework.stereotype.Service;

import com.richard.demo.services.RemoteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RemoteServiceImpl implements RemoteService {
    @Override
    public int process(int i) {
        if (i < 10) {
            return i * i;
        } else {
            log.warn(String.format("Simulated remote service failure for input: %d", i));
            throw new RuntimeException("Simulated remote service failure for input: " + i);
        }
    }
}

